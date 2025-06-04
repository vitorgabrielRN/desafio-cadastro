
 // criar o database antes de rodar o codigo       
                                                  SELECT * FROM cliente;
                                                  SELECT * FROM fatura;
 
CREATE DATABASE desafioProdiga
  WITH ENCODING 'UTF8'
  LC_COLLATE = 'pt_BR.UTF-8'
  LC_CTYPE = 'pt_BR.UTF-8';

  endpoints via  postman ou url body(form - data)
 http://localhost:8080/clientes ,   http://localhost:8080/clientes/novo , http://localhost:8080/faturas/cliente/1
 	
Clientes ----------------------------------------    
                    Endpoint	
GET	/clientes	             Lista todos os clientes
GET	/clientes/novo	         Mostra formulário de novo cliente
POST	/clientes/salvar	 Cadastra um novo cliente
GET	/clientes/editar/{id}	 Mostra formulário de edição
GET	/clientes/excluir/{id}	 Remove um cliente


Faturas -----------------------------------------

                    Endpoint	
GET	/faturas/gerar	                Mostra formulário para gerar fatura
POST	/faturas/gerar	            Gera uma nova fatura
GET	/faturas/cliente/{clienteId}	Lista faturas de um cliente


Exemplo de requisição

nome: vitor gabriel rodrigues do nascimento
cpf: 019.056.362 - 10
email: malaquitovitor@email.com
telefone: (91) 984040825
logradouro: trv. Uberaba
bairro: Tapana
cep: 66825-020
cidade: Para
uf: pa



para buscar no banco de dados



select * from clientes;
select * from faturas;
SELECT
    c.nome AS nome_cliente,
    c.cpf AS cpf_cliente,
    f.numero_fatura,
    f.mes_ano_referencia,
    f.valor,
    f.data_vencimento,
    f.situacao,
    f.codigo_boleto,
    f.data_pagamento
FROM
    faturas f
JOIN
    clientes c ON f.cliente_id = c.id
ORDER BY
    c.nome, f.data_vencimento;