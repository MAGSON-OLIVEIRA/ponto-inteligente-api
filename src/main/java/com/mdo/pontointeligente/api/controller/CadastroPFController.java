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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdo.pontointeligente.api.dto.CadastroPFDto;
import com.mdo.pontointeligente.api.entities.Empresa;
import com.mdo.pontointeligente.api.entities.Funcionario;
import com.mdo.pontointeligente.api.enums.PerfilEnum;
import com.mdo.pontointeligente.api.response.Response;
import com.mdo.pontointeligente.api.services.EmpresaService;
import com.mdo.pontointeligente.api.services.FuncionarioService;

@RestController
@RequestMapping("api/cadastro-pf")
@CrossOrigin(origins="*")
public class CadastroPFController {
	
	private static final Logger log = LoggerFactory.getLogger(CadastroPFController.class);
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	public CadastroPFController() {
		
	}

	@PostMapping
	public ResponseEntity<Response<CadastroPFDto>> cadastrar(@Valid @RequestBody CadastroPFDto dto,
			BindingResult result) {
		Response<CadastroPFDto> cadastroPFDto = new Response<CadastroPFDto>();
		validarDadosExistentes(dto, result);
		Funcionario funcionario = new Funcionario();
		extractedFuncionario(dto, funcionario);
		if (result.hasErrors()) {
			log.info("Error");
			result.getAllErrors().forEach(error -> cadastroPFDto.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(cadastroPFDto);
		}
		Optional<Empresa> empresa = empresaService.buscarPorCnpj(dto.getCnpj());
		empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
		this.funcionarioService.persistir(funcionario);
		cadastroPFDto.setData(extractedDto(funcionario));
		return ResponseEntity.ok(cadastroPFDto);
	}


	private CadastroPFDto extractedDto(Funcionario funcionario) {
		CadastroPFDto dto = new CadastroPFDto();
		dto.setId(funcionario.getId());
		dto.setCnpj(funcionario.getEmpresa().getCnpj());
		dto.setCpf(funcionario.getCpf());
		dto.setEmail(funcionario.getEmail());
		dto.setNome(funcionario.getNome());
		dto.setSenha(funcionario.getSenha());
		funcionario.getValorHoraOpt().ifPresent(valor -> dto.setValorHora(Optional.of(valor.toString())));
		funcionario.getQtdHorasAlmocoOpt().ifPresent(valor -> dto.setQtdHorasAlmoco(Optional.of(Float.toString(valor))));
		funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(valor -> dto.setQtdHorasTrabalhadaDia(Optional.of(Float.toString(valor))));
		return dto;
	}


	private void extractedFuncionario(CadastroPFDto dto, Funcionario funcionario) {
		funcionario.setCpf(dto.getCpf());
		funcionario.setEmail(dto.getEmail());
		funcionario.setNome(dto.getNome());
		funcionario.setSenha(dto.getSenha());
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		dto.getValorHora().ifPresent(valor -> funcionario.setValorHora(new BigDecimal(valor)));
		dto.getQtdHorasAlmoco().ifPresent(valor -> funcionario.setQtdHorasAlmoco(Float.parseFloat(valor)));
		dto.getQtdHorasTrabalhadaDia().ifPresent(valor -> funcionario.setQtdHorasTrabalhoDia(Float.parseFloat(valor)));
	}



	private void validarDadosExistentes(CadastroPFDto dto, BindingResult result) {
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(dto.getCnpj());
		if (!empresa.isPresent()) {
			result.addError(new ObjectError("Cnpj: ", "Cnpj Não cadastrado."));
		}
		this.funcionarioService.findByCpf(dto.getCpf())
			.ifPresent(funcionario -> result.addError(new ObjectError("CPF:", "CPF já existente.")));
		this.funcionarioService.findByEmail(dto.getEmail())
			.ifPresent(funcionario -> result.addError(new ObjectError("E-mail:", "E-mail já existente.")));
		
	}
	
	

}
