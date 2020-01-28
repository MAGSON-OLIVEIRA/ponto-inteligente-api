package com.mdo.pontointeligente.api.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.mdo.pontointeligente.api.entities.Funcionario;

//transactional ajunda na perfomace  quando for consulta. como os 3 metodos cusotmizados são de apenas consulta. 
@Transactional(readOnly = true)
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
	Funcionario findByCpf(String cpf);
	Funcionario findByEmail(String email);
	//Funcionario findByCpfOrEmail(String cpf, String email);
}
