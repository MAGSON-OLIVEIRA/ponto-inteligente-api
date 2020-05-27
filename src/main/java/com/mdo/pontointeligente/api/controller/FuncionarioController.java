package com.mdo.pontointeligente.api.controller;

import java.math.BigDecimal;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdo.pontointeligente.api.dto.FuncionarioDto;
import com.mdo.pontointeligente.api.entities.Funcionario;
import com.mdo.pontointeligente.api.response.Response;
import com.mdo.pontointeligente.api.services.FuncionarioService;
import com.mdo.pontointeligente.api.utils.PasswordUtils;

@RestController   // anotação controler
@RequestMapping("/api/funcionarios") // mapeado 
@CrossOrigin("*")
public class FuncionarioController {
	
	private static final Logger log = LoggerFactory.getLogger(FuncionarioController.class);
	
	@Autowired // solicitar injeção de dependencia. 
	private FuncionarioService funcionarioService;
	
	
	public FuncionarioController() {
	}
	
	@PutMapping(value="/{id}")  //  uso do put para uma Atualização do funcionario. 
	public ResponseEntity<Response<FuncionarioDto>> atualizar(@PathVariable("id") Long id ,  // pegar o valor do {id}
			@Valid @RequestBody FuncionarioDto funcionarioDto, BindingResult result) throws NoSuchFieldException{
			
		log.info("Atualização de funcionario: {}", funcionarioDto.toString());
		Response<FuncionarioDto> response = new Response<FuncionarioDto>();
		
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(id);
		if(!funcionario.isPresent()) {
			result.addError(new ObjectError("funcionario", "Funcionário não encontrado."));
		}
		this.atualizarDadosFuncionario(funcionario.get(), funcionarioDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando funcionario", result.getAllErrors());
			result.getAllErrors().forEach(erro -> response.getErrors().add(erro.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.funcionarioService.persistir(funcionario.get());
		response.setData(this.converterFuncionarioDto(funcionario.get()));
		
		return ResponseEntity.ok(response);
	}

	private FuncionarioDto converterFuncionarioDto(Funcionario funcionario) {
		FuncionarioDto dto = new FuncionarioDto();
		dto.setId(funcionario.getId());
		dto.setEmail(funcionario.getEmail());
		dto.setNome(funcionario.getNome());
		funcionario.getQtdHorasAlmocoOpt()
				.ifPresent(qtdHa -> dto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHa))));
		
		funcionario.getQtdHorasTrabalhoDiaOpt()
				.ifPresent(qtdHtd -> dto.setQtdHorasTravalhoDia(Optional.of(Float.toString(qtdHtd))));
		
		funcionario.getValorHoraOpt()
				.ifPresent(val -> dto.setValorHora(Optional.of(val.toString())));
		
		return dto;
	}

	private void atualizarDadosFuncionario(Funcionario funcionario, @Valid FuncionarioDto funcionarioDto,
			BindingResult result) {
		funcionario.setNome(funcionarioDto.getNome());
		if(!funcionario.getEmail().equals(funcionarioDto.getEmail())) {
			this.funcionarioService.findByEmail(funcionarioDto.getEmail())
				.ifPresent(func -> result.addError(new ObjectError("email", "E-mail já existente."))); // if se emal ja cadastrado erro
			// senão atualizar o email do funcionario.
			funcionario.setEmail(funcionarioDto.getEmail());
		}
		
		funcionario.setQtdHorasAlmoco(null);
		funcionarioDto.getQtdHorasAlmoco()
				.ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		
		funcionario.setQtdHorasTrabalhoDia(null);
		funcionarioDto.getQtdHorasTravalhoDia()
				.ifPresent(qtHorasTdia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtHorasTdia)));
		
		funcionario.setValorHora(null);
		funcionarioDto.getValorHora()
				.ifPresent(vlr -> funcionario.setValorHora(new BigDecimal(vlr)));
				
		if(funcionarioDto.getSenha().isPresent()) {
			funcionario.setSenha(PasswordUtils.gerarBCrypt(funcionarioDto.getSenha().get()));
		}
		
	}
	

}
