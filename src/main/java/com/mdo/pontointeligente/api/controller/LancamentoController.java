package com.mdo.pontointeligente.api.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mdo.pontointeligente.api.dto.LancamentoDto;
import com.mdo.pontointeligente.api.entities.Funcionario;
import com.mdo.pontointeligente.api.entities.Lancamento;
import com.mdo.pontointeligente.api.enums.TipoEnum;
import com.mdo.pontointeligente.api.response.Response;
import com.mdo.pontointeligente.api.services.FuncionarioService;
import com.mdo.pontointeligente.api.services.LancamentoService;

@RestController
@RequestMapping("/api/lancamentos")
@CrossOrigin("*")
public class LancamentoController {
	
	private static final Logger log = LoggerFactory.getLogger(LancamentoController.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	@Autowired // solicitar injeção de dependencia. 
	private LancamentoService lancamentoService;
	
	@Autowired // solicitar injeção de dependencia. 
	private FuncionarioService funcionarioService;
	
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;
	
	
	public LancamentoController() {
	}
	
	
	/**
	 * 
	 * @param funcionarioId
	 * @param pag
	 * @param ord
	 * @param dir
	 * @return
	 */
	@GetMapping(value="/funcionario/{funcionarioId}")
	public ResponseEntity<Response<Page<LancamentoDto>>> listarLancamentosPorIdFuncionario(
			@PathVariable("funcionarioId") Long funcionarioId,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir
			
			){
		log.info("Buscando lancamento por id funcionario: {}, pagina {}", funcionarioId, pag);
		Response<Page<LancamentoDto>> response = new Response<Page<LancamentoDto>>();
		
		PageRequest pageRequest = PageRequest.of(pag, this.qtdPorPagina, Direction.valueOf(dir), ord);
		Page<Lancamento> lancamentos = this.lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest);
		Page<LancamentoDto> lancamentoDto = lancamentos.map(lancamento -> this.converterLancamentoDto(lancamento));  // map 
		response.setData(lancamentoDto);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value="/funcionario/{funcionarioEmail}/ultimo")
	public ResponseEntity<Response<LancamentoDto>> listarLancamentosPorEmialFuncionario(
			@PathVariable("funcionarioEmail") String funcionarioEmail){
		log.info("Buscando lancamento por email funcionario: {}, pagina {}", funcionarioEmail);
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		LancamentoDto lancamentoDto = new LancamentoDto();
		Lancamento lancamento = this.lancamentoService.findByFuncionarioEmail(funcionarioEmail);
		if(lancamento!=null) {
			lancamentoDto =  this.converterLancamentoDto(lancamento);  // map 
		}
		response.setData(lancamentoDto);
		return ResponseEntity.ok(response);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping(value="/{id}")
	public ResponseEntity<Response<LancamentoDto>> listarPorId(@PathVariable("id") Long id){
		log.info("Buscando lançamento por ID: {}", id);
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		Optional<Lancamento> lancamento = this.lancamentoService.burcarPorId(id);
		if(!lancamento.isPresent()) {
			log.error("Buscando lançamento por ID: {}", id);
			response.getErrors().add("Lancamento não encontrado "+id);
			return ResponseEntity.badRequest().body(response);
		}
		response.setData(this.converterLancamentoDto(lancamento.get()));
		return ResponseEntity.ok(response);
		
	}
	
	/**
	 * 
	 * @param lancamentoDto
	 * @param result
	 * @return
	 * @throws ParseException
	 */
	@PostMapping
	public ResponseEntity<Response<LancamentoDto>> adicionar(@Valid @RequestBody LancamentoDto lancamentoDto,
			BindingResult result) throws ParseException{
		log.info("Adicionando lançamento {} "+lancamentoDto.toString());
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		this.validarFuncionario(lancamentoDto, result);
		Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando lancamento {}" , result.getAllErrors());
			result.getAllErrors().forEach(erro -> response.getErrors().add(erro.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		lancamento = this.lancamentoService.persistir(lancamento);
		response.setData(this.converterLancamentoDto(lancamento));
		return ResponseEntity.ok(response);
	}
	

	/**
	 * 
	 * @param id
	 * @param lancamentoDto
	 * @param result
	 * @return
	 * @throws ParseException
	 */
	@PutMapping(value="/{id}")
	public ResponseEntity<Response<LancamentoDto>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody LancamentoDto lancamentoDto, BindingResult result
			) throws ParseException{
		
		log.info("Alterando lançamento {} "+lancamentoDto.toString());
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		this.validarFuncionario(lancamentoDto, result);
		lancamentoDto.setId(Optional.of(id));
		Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando lancamento {}" , result.getAllErrors());
			result.getAllErrors().forEach(erro -> response.getErrors().add(erro.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		lancamento = this.lancamentoService.persistir(lancamento);
		response.setData(this.converterLancamentoDto(lancamento));
		return ResponseEntity.ok(response);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping(value="/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id){
		log.info("Remover lançamento {}", id);
		Response<String> response = new Response<String>();
		Optional<Lancamento> lancamento = this.lancamentoService.burcarPorId(id);
		
		if(!lancamento.isPresent()) {
			log.info("Codigo invalido, erro remover registro. {}", id);
			response.getErrors().add("Erro remover lançamento"+id);
			return ResponseEntity.badRequest().body(response);
		}
		this.lancamentoService.remover(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	/**
	 * 
	 * @param lancamentoDto
	 * @param result
	 * @return
	 * @throws ParseException
	 */
	private Lancamento converterDtoParaLancamento(LancamentoDto lancamentoDto, BindingResult result) throws ParseException {
		Lancamento lancamento = new Lancamento();
		if(lancamentoDto.getId().isPresent()) {
			Optional<Lancamento> lanc = this.lancamentoService.burcarPorId(lancamentoDto.getId().get());
			
			if(lanc.isPresent()) {
				lancamento = lanc.get();
			}else {
				result.addError(new ObjectError("lancamento","Lancamento não encontrado."));
			}
		}else {
			lancamento.setFuncionario(new Funcionario());
			lancamento.getFuncionario().setId(lancamentoDto.getFuncionarioId());
		}
		lancamento.setDescricao(lancamentoDto.getDescricao());
		lancamento.setLocalizacao(lancamentoDto.getLocalizacao());
		lancamento.setData(this.dateFormat.parse(lancamentoDto.getData()));
		
		if(EnumUtils.isValidEnum(TipoEnum.class, lancamentoDto.getTipo())) {
			lancamento.setTipo(TipoEnum.valueOf(lancamentoDto.getTipo()));
		}else {
			result.addError(new ObjectError("tipo", "tipo invalido"));
		}
		
		return lancamento;
	}


	/**
	 * 
	 * @param lancamentoDto
	 * @param result
	 */
	private void validarFuncionario(LancamentoDto lancamentoDto, BindingResult result) {
		if(lancamentoDto.getFuncionarioId()==null) {
			result.addError(new ObjectError("funcionario","Funcionário não informado"));
			return;
		}
		log.info("validando funcionario id {} ", lancamentoDto.getFuncionarioId());
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(lancamentoDto.getFuncionarioId());
		if(!funcionario.isPresent()) {
			result.addError(new ObjectError("funcionario", "Funcionario não encontrado"));
		}
	}



	/**
	 * 
	 * @param lancamento
	 * @return
	 */
	private LancamentoDto converterLancamentoDto(Lancamento lancamento) {
		LancamentoDto lancamentoDto = new LancamentoDto();
		lancamentoDto.setId(Optional.of(lancamento.getId()));
		lancamentoDto.setData(this.dateFormat.format(lancamento.getData()));
		lancamentoDto.setTipo(lancamento.getTipo().toString());
		lancamentoDto.setDescricao(lancamento.getDescricao());
		lancamentoDto.setLocalizacao(lancamento.getLocalizacao());
		lancamentoDto.setFuncionarioId(lancamento.getFuncionario().getId());
		return lancamentoDto;
	}
	

}
