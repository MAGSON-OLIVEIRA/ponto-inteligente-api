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
import com.mdo.pontointeligente.api.entities.Funcionario;
import com.mdo.pontointeligente.api.repositories.FuncionarioRepository;
import com.mdo.pontointeligente.api.services.FuncionarioService;

//carregar o contexto de texto do spring
//carregando o profiles de "test"
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioServiceTest {
	// usar o MockBean para mockar o repositorio, por que a classe reposytory ja foi testada em outro arquivo de test. 
	// mocka o repository, então vamos somente simular suas chamadas. 
	@MockBean
	private FuncionarioRepository funcionarioRepository;
	
	// vamos usar uma stancia do service para testarmos. 
	@Autowired
	private FuncionarioService funcionarioService;
	
	
	
	@Before
	public void setUp() throws Exception {
		// criar os mock para as chamatas que vamos usar. do repository
		// usar o BDD Mockito
		Funcionario func = new Funcionario();
		Optional<Funcionario>  funcionario = Optional.ofNullable(func);
		BDDMockito.given(this.funcionarioRepository.save(Mockito.any(Funcionario.class))).willReturn(new Funcionario());
		BDDMockito.given(this.funcionarioRepository.findById(Mockito.anyLong())).willReturn(funcionario);
		BDDMockito.given(this.funcionarioRepository.findByCpf(Mockito.anyString())).willReturn(new Funcionario());
		BDDMockito.given(this.funcionarioRepository.findByEmail(Mockito.anyString())).willReturn(new Funcionario());
		
	}
	
	
	@Test
	public void salver() throws Exception{
		Funcionario funcionario = this.funcionarioService.persistir(new Funcionario());
		assertNotNull(funcionario);
	}
	
	@Test
	public void buscarPorId() throws Exception{
		Optional<Funcionario> funcionario =  this.funcionarioService.buscarPorId(1l);
		assertTrue(funcionario.isPresent());
	}
	
	@Test
	public void buscarPorEmail() throws Exception{
		Optional<Funcionario> funcionario =  this.funcionarioService.findByEmail("teste@email.com");
		assertTrue(funcionario.isPresent());
	}
	
	@Test
	public void buscarPorcpf() throws Exception{
		Optional<Funcionario> funcionario =  this.funcionarioService.findByCpf("00704907160");
		assertTrue(funcionario.isPresent());
	}
	
	
}
