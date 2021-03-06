package com.mdo.pontointeligente.api.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdo.pontointeligente.api.dto.EmpresaDto;
import com.mdo.pontointeligente.api.entities.Empresa;
import com.mdo.pontointeligente.api.response.Response;
import com.mdo.pontointeligente.api.services.EmpresaService;



@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins="*")
public class EmpresaController {
	
	private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);
	
	@Autowired
	private EmpresaService empresaService;
		
	public EmpresaController() {
		
	}

	/**
	 * 
	 * @param cnpj
	 * @return
	 */
	@GetMapping(value = "/cnpj/{cnpj}")
	public ResponseEntity<Response<EmpresaDto>> getEmpresaPorCnpj(@PathVariable("cnpj") String cnpj){
		log.info("Buscando empresa por cnpj {}"+ cnpj);
		Response<EmpresaDto> response = new Response<EmpresaDto>();
		Optional<Empresa> empresa = empresaService.buscarPorCnpj(cnpj);
		if(!empresa.isPresent()) {
			log.info("Empresa não encontrada para o CNPJ: {}", cnpj);
			response.getErrors().add("Empresa não encontrada para o CNPJ: "+cnpj);
			return ResponseEntity.badRequest().body(response);
		}
		response.setData(extractedEmrpesaDto(empresa.get()));
		return ResponseEntity.ok(response);
	}
	private EmpresaDto extractedEmrpesaDto(Empresa empresa) {	
		EmpresaDto dtoEmpresa = new EmpresaDto();
		dtoEmpresa.setCnpj(empresa.getCnpj());
		dtoEmpresa.setId(empresa.getId());
		dtoEmpresa.setRazaoSocial(empresa.getRazaoSocial());
		return dtoEmpresa;
	}

	

}
