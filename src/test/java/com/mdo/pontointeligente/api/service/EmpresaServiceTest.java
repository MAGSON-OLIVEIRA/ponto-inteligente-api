package com.mdo.pontointeligente.api.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.mdo.pontointeligente.api.entities.Empresa;
import com.mdo.pontointeligente.api.repositories.EmpresaRepository;
import com.mdo.pontointeligente.api.services.EmpresaService;
// carregar o contexto de texto do spring
// carregando o profiles de "test"
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmpresaServiceTest {
	// usar o MockBean para mockar o repositorio, por que a classe reposytory ja foi testada em outro arquivo de test. 
	// mocka o repository, então vamos somente simular suas chamadas. 
	@MockBean
	private EmpresaRepository empresaRepository;
	
	// vamos usar uma stancia do service para testarmos. 
	@Autowired
	private EmpresaService empresaService;
	
	
	
	@Before
	public void setUp() throws Exception {
		// criar os mock para as chamatas que vamos usar. do repository
		// usar o BDD Mockito
		BDDMockito.given(this.empresaRepository.save(Mockito.any(Empresa.class))).willReturn(new Empresa());
		BDDMockito.given(this.empresaRepository.findByCnpj(Mockito.anyString())).willReturn(new Empresa());

	}
	
	@Test
	public void salvar()throws Exception{
		Empresa empresa = this.empresaService.persistir(new Empresa());
		assertNotNull(empresa);
	}
	
	@Test
	public void buscarPorCnpj() throws Exception {
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj("");
		assertTrue(empresa.isPresent());
	}
	
	
}
