package com.mdo.pontointeligente.api.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.mdo.pontointeligente.api.entities.Lancamento;
import com.mdo.pontointeligente.api.repositories.LancamentoRepository;
import com.mdo.pontointeligente.api.services.LancamentoService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoServiceTest {
	
	@MockBean
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Before
	public void setUp() throws Exception{
		Optional<Lancamento> lancamentoMock = Optional.ofNullable(new Lancamento());
		BDDMockito.given(this.lancamentoRepository.save(Mockito.any(Lancamento.class))).willReturn(new Lancamento());
		BDDMockito.given(this.lancamentoRepository.findByFuncionarioId(Mockito.anyLong(), Mockito.any(PageRequest.class))).willReturn(new PageImpl<Lancamento>(new ArrayList<Lancamento>()));
		BDDMockito.given(this.lancamentoRepository.findById(Mockito.anyLong())).willReturn(lancamentoMock);
	}
	
	@Test
	public void salvar() throws Exception{
		Lancamento lancamento = lancamentoService.persistir(new Lancamento());
		assertNotNull(lancamento);
	}
	
	@Test
	public void bucarPorIdFuncionario() throws Exception{
		Page<Lancamento> listaLancamento = this.lancamentoService.buscarPorFuncionarioId(1l, new PageRequest(0, 10));
		assertNotNull(listaLancamento);
	}
	
	@Test
	public void bucarPorId() throws Exception{
		Optional<Lancamento> lancamento = this.lancamentoService.burcarPorId(1l);
		assertTrue(lancamento.isPresent());
	}
	
	
}
