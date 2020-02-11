package com.mdo.pontointeligente.api.controller;

import java.util.Date;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdo.pontointeligente.api.dto.CadastroPJDto;
import com.mdo.pontointeligente.api.dto.EmpresaDto;
import com.mdo.pontointeligente.api.entities.Empresa;
import com.mdo.pontointeligente.api.entities.Funcionario;
import com.mdo.pontointeligente.api.enums.PerfilEnum;
import com.mdo.pontointeligente.api.response.Response;
import com.mdo.pontointeligente.api.services.EmpresaService;
import com.mdo.pontointeligente.api.services.FuncionarioService;
import com.mdo.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pj")
@CrossOrigin(origins="*")
public class CadastroPJController {
	
	private static final Logger log = LoggerFactory.getLogger(CadastroPJController.class);
	
	@Autowired
	private FuncionarioService funcionarioService;
	@Autowired
	private EmpresaService empresaService;
	
	
	public CadastroPJController() {
		
	}
	
	@PostMapping("/salvar")
	public ResponseEntity<Response<CadastroPJDto>> cadastrar(@Valid @RequestBody CadastroPJDto cadastroPJDto,
			BindingResult result) {
		log.info("Cadastro P.J. : {}", cadastroPJDto.toString());
		Response<CadastroPJDto> response = new Response<CadastroPJDto>();
		validaDadosExistentes(cadastroPJDto, result);
		
		Empresa empresa = this.converterDtoParaEmpresa(cadastroPJDto);
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPJDto);
		// validar os erro 
		if(result.hasErrors()) {
			log.error("Erro validando dados de cadastro PJ: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		this.empresaService.persistir(empresa);
		funcionario.setEmpresa(empresa);
		this.funcionarioService.persistir(funcionario);
		response.setData(cadastroPJDto);
		return ResponseEntity.ok(response);
	}
	

	private Empresa converterDtoParaEmpresa(CadastroPJDto cadastroPJDto) {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial(cadastroPJDto.getRazaoSocial());
		empresa.setCnpj(cadastroPJDto.getCnpj());
		empresa.setDataCriacao(new Date());
		return empresa;
	}
	private Funcionario converterDtoParaFuncionario(CadastroPJDto cadastroPJDto) {
		Funcionario funcionario = new Funcionario();
		funcionario.setCpf(cadastroPJDto.getCpf());
		funcionario.setEmail(cadastroPJDto.getEmail());
		funcionario.setNome(cadastroPJDto.getNome());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPJDto.getSenha()));
		
		return funcionario;
	}

	private void validaDadosExistentes(CadastroPJDto cadastroPJDto, BindingResult result) {
		this.empresaService.buscarPorCnpj(cadastroPJDto.getCnpj())
			.ifPresent(emp -> result.addError(new ObjectError("Empresa", "Empresa já existente")));
		this.funcionarioService.findByCpf(cadastroPJDto.getCpf())
			.ifPresent(func -> result.addError(new ObjectError("Funcionario", "CPF Já existente.")));
		this.funcionarioService.findByEmail(cadastroPJDto.getEmail())
		.ifPresent(func -> result.addError(new ObjectError("Funcionario", "E-mail Já existente.")));
		
	}

}
