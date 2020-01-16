package com.mdo.pontointeligente.api.services;

import java.util.Optional;

import com.mdo.pontointeligente.api.entities.Empresa;

public interface EmpresaService {
	// assinatura do método para implemntação da classe que a implementar. 
	/**
	 * 
	 * @param cnpj
	 * @return
	 */
	Optional<Empresa> buscarPorCnpj(String cnpj);
	/**
	 * 
	 * @param empresa
	 * @return
	 */
	Empresa persistir(Empresa empresa);

}
