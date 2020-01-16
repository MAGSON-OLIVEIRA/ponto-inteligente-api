package com.mdo.pontointeligente.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import com.mdo.pontointeligente.api.entities.Empresa;

@Transactional(readOnly = true)
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
	
	// transactional ajuda na perfomace  quando for consulta. Nao bloqueia o banco de dado 
	// como os 3 metodos cusotmizados são de apenas consulta. 
	@Transactional(readOnly = true)
	Empresa findByCnpj(String cnpj);
	
	
}

