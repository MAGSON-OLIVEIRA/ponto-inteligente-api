package com.mdo.pontointeligente.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.mdo.pontointeligente.api.entities.Lancamento;

public interface LancamentoService {
	
	/**
	 * Buscar lançamento por id do funcionario com pageRequest
	 * @param funcionarioId
	 * @param pageRequest
	 * @return Page<Lancamento>
	 */
	Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest);
	
	/**
	 * Buscar lançamento por id. 
	 * @param id
	 * @return Optional<Lancamento>
	 */
	Optional<Lancamento> burcarPorId(Long id);
	
	/**
	 * Salvar um lançamento
	 * @param lancamento
	 * @return Lancamento
	 */
	Lancamento persistir(Lancamento lancamento);
	
	/**
	 * Remover um lançamento. 
	 * @param id
	 */
	void remover(Long id);
	
	/**
	 * 
	 * @param funcionarioEmail
	 * @param pageRequest
	 * @return
	 */
	List<Lancamento> findByFuncionarioEmail(String funcionarioEmail);
	
}
