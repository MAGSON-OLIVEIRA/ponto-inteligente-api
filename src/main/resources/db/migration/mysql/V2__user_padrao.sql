INSERT INTO `ponto_inteligente`.`empresa` (`id`,`cnpj`, `data_atualizacao`, `data_criacao`, `razao_social`) 
VALUES ('999999999','00000000000001', '2020-05-28 20:43:35', '2020-05-28 20:43:43', 'EMPRESA MASTE');

INSERT INTO `ponto_inteligente`.`funcionario` (`id`,`cpf`, `data_atualizacao`, `data_criacao`, `email`, `nome`, `perfil`, `senha`, `empresa_id`) 
VALUES ('99999999999','00000000001', '2020-05-28 20:46:26', '2020-05-28 20:46:28', 'admin@admin.com', 'admin', 'ROLE_ADMIN', '$2a$10$sZE6sTBlrLvRxm3iwWbzBempi0t28Sj1/EXXGO.MTi4Bk52wz8W8m', '999999999');
