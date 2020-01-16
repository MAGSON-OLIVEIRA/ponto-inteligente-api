package com.mdo.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdo.pontointeligente.api.entities.Empresa;
import com.mdo.pontointeligente.api.repositories.EmpresaRepository;
import com.mdo.pontointeligente.api.services.EmpresaService;

// anotação pra definir classe como serviço
@Service
public class EmpresaServiceImpl  implements EmpresaService {
	
	// adicionando log como boas praticas
	private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);
	
	@Autowired
	EmpresaRepository empresaRepository;

	@Override
	public Optional<Empresa> buscarPorCnpj(String cnpj) {
		log.info("buscar uma empresa por cnpj {}", cnpj );
		return Optional.ofNullable(empresaRepository.findByCnpj(cnpj));
	}

	@Override
	public Empresa persistir(Empresa empresa) {
		log.info("salvar empresa na base {} ", empresa.toString());
		return this.empresaRepository.save(empresa);
	}

}
