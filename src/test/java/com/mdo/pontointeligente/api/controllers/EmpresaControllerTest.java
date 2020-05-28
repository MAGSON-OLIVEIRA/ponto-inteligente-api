package com.mdo.pontointeligente.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.mdo.pontointeligente.api.entities.Empresa;
import com.mdo.pontointeligente.api.services.EmpresaService;

@RunWith(SpringRunner.class)
@SpringBootTest // criar o contexto sprinboot
@ActiveProfiles("test") // habilitar configuração de test
@AutoConfigureMockMvc // adicionado a parte web, foi adicionado um contexto web. novo contexto web
public class EmpresaControllerTest {

	// obter uma instancia mockmvc
	// conteiner web
	@Autowired
	private MockMvc mvc;
	
	@MockBean // mocar a empresa sevice
	private EmpresaService empresaService;
	
	private static final String BUSCAR_EMPRESA_CNPJ_URL = "/api/empresas/cnpj/";   // url pra busca por cnpj
	private static final Long ID = Long.valueOf(1);
	private static final String CNPJ="07943503000140";
	private static final String RAZAO_SOCIAL = "OK EMPRESA";
	private static final String VALUE_MSN = "Empresa não encontrada para o CNPJ: ";
	
	@Test
	@WithMockUser
	public void testBuscarEmpresaCnpjInvalidoValido()throws Exception{
		// Optional.empty para simular um retorno que não foi encontrado nada. 
		BDDMockito.given(this.empresaService.buscarPorCnpj(Mockito.anyString())).willReturn(Optional.empty()); // sumular o retorno vazio. 
		
		// moc o perform -> execute 
		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_EMPRESA_CNPJ_URL+CNPJ).accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest())  // defino que o retorno é um badRequest 400  
					.andExpect(jsonPath("$.errors").value(VALUE_MSN+CNPJ)); // jsonPath query em json para acessar o erro do retorno com erro. 
	}
	
	@Test
	@WithMockUser
	public void testBuscarCNPJValido()throws Exception{
		// Obter uma empresa no retorno para realizar o buscar de uma empresa valida. 
		BDDMockito.given(this.empresaService.buscarPorCnpj(Mockito.anyString()))
							.willReturn(Optional.of(this.obterEmpresa())); // retorna o objeto carregado no obterEmpresa
		
		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_EMPRESA_CNPJ_URL+CNPJ)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()) // primeira verificar ok defino com 200 
				.andExpect(jsonPath("$.data.id").value(ID)) // pegar o valor via jsonPath query 
				.andExpect(jsonPath("$.data.razaoSocial").value(RAZAO_SOCIAL))
				.andExpect(jsonPath("$.data.cnpj").value(CNPJ))
				.andExpect(jsonPath("$.errors").isEmpty()); // definindo o erro como vazio
	}

	private Empresa obterEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setId(ID);
		empresa.setRazaoSocial(RAZAO_SOCIAL);
		empresa.setCnpj(CNPJ);
		return empresa;
	}

}
