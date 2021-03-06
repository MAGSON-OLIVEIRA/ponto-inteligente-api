package com.mdo.pontointeligente.api.services.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.mdo.pontointeligente.api.entities.Lancamento;
import com.mdo.pontointeligente.api.repositories.LancamentoRepository;
import com.mdo.pontointeligente.api.services.LancamentoService;

// notação para definir classe como servico
@Service
public class LancamentoServiceImpl implements LancamentoService {
	// log infomativo, boa praticas. 
	private static final Logger log = LoggerFactory.getLogger(LancamentoServiceImpl.class);
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageable) {
		log.info("Buscar lançamento por id {} funcionario", funcionarioId);
		return lancamentoRepository.findByFuncionarioId(funcionarioId, pageable);
	}

	@Override
	@Cacheable("lancamentoPorId")
	public Optional<Lancamento> burcarPorId(Long id) {
		log.info("Buscar lançamento por id {}", id);
		return  lancamentoRepository.findById(id);
	}

	@Override
	@Cacheable("lancamentoPorId")
	public Lancamento persistir(Lancamento lancamento) {
		log.info("Salvar lançamento", lancamento.toString());
		return lancamentoRepository.save(lancamento);
	}

	@Override
	public void remover(Long id) {
		log.info("Remover lançamendo id {}", id);
		lancamentoRepository.deleteById(id);
	}
	
	@Override
	public Lancamento findByFuncionarioEmail(String funcionarioEmail) {
		log.info("Buscar lançamento por id {} funcionario", funcionarioEmail);
		String sql = "select * from lancamento l, funcionario f where l.funcionario_id = f.id and Date(l.data) = CURDATE() and f.email = '"+funcionarioEmail+"' and l.id = (SELECT max(lanc.id) FROM lancamento lanc)  ";
		Lancamento lanc =(Lancamento) manager.createNativeQuery(sql, Lancamento.class).getSingleResult();
		return lanc;
	}
}
