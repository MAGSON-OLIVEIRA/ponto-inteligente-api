package com.mdo.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdo.pontointeligente.api.entities.Funcionario;
import com.mdo.pontointeligente.api.repositories.EmpresaRepository;
import com.mdo.pontointeligente.api.repositories.FuncionarioRepository;
import com.mdo.pontointeligente.api.services.FuncionarioService;

//anotação pra definir classe como serviço
@Service
public class FuncionarioServiceImpl implements FuncionarioService {
	// adicionando log como boas praticas
	private static final Logger log = LoggerFactory.getLogger(FuncionarioServiceImpl.class);
	
	@Autowired
	EmpresaRepository empresaRepository;
	
	@Autowired
	FuncionarioRepository funcionarioRepository;

	@Override
	public Optional<Funcionario> findByCpf(String cpf) {
		log.info("pesquisa por cpf {} ", cpf);
		return Optional.ofNullable(funcionarioRepository.findByCpf(cpf));
	}

	@Override
	public Optional<Funcionario> findByEmail(String email) {
		log.info("pesquisa funcionario por e-mail {}", email);
		return Optional.ofNullable(funcionarioRepository.findByEmail(email));
	}

	@Override
	public Optional<Funcionario> findByCpfOrEmail(String cpf, String email) {
		log.info("pesquisa funcionario por e-mail e cpf {} {}", cpf, email);
		return Optional.ofNullable(funcionarioRepository.findByCpfOrEmail(cpf, email));
	}

	@Override
	public Funcionario persistir(Funcionario funcionario) {
		log.info("persistir funcionario na base:", funcionario.toString());
		return this.funcionarioRepository.save(funcionario);
	}

	@Override
	public Optional<Funcionario> buscarPorId(Long id){
		log.info("pesquisa funcionario por id {}", id);
		return Optional.ofNullable(funcionarioRepository.findById(id).get());
	}
	
	
}
