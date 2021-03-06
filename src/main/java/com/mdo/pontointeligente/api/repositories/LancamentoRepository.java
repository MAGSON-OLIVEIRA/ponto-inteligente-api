package com.mdo.pontointeligente.api.repositories;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.mdo.pontointeligente.api.entities.Lancamento;

@Transactional(readOnly = true)
@NamedQueries({
	@NamedQuery(name="LancamentoRepository.findByFuncionarioId", query = "Select lanc from Lancamento lanc where lanc.funcionario.id = :funcionarioId"),
	@NamedQuery(name="LancamentoRepository.findByFuncionarioEmail", query = "Select lanc from Lancamento lanc where lanc.funcionario.email = funcionarioEmail")
})
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	List<Lancamento> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId);
	Page<Lancamento> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId, Pageable pageable);
	List<Lancamento> findByFuncionarioEmail(@Param("funcionarioEmail") String email);
	
}
