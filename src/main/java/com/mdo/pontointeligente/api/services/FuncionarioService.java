package com.mdo.pontointeligente.api.services;

import java.util.Optional;

import com.mdo.pontointeligente.api.entities.Funcionario;

public interface FuncionarioService {

	/**
	 * @param cpf
	 * @return
	 */
	Optional<Funcionario> findByCpf(String cpf);

	/**
	 * @param email
	 * @return
	 */
	Optional<Funcionario> findByEmail(String email);

	/**
	 * @param cpf
	 * @param email
	 * @return
	 */
//	Optional<Funcionario> findByCpfOrEmail(String cpf, String email);

	/**
	 * Salvar funcionario
	 * @param funcionario
	 * @return
	 */
	Funcionario persistir(Funcionario funcionario);
	
	/**
	 * Buscar por ID 
	 * 
	 */
	public Optional<Funcionario> buscarPorId(Long id);
	
}
