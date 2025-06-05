
CREATE VIEW faturas_com_clientes AS
SELECT
    f.id AS fatura_id,
    f.numero_fatura,
    f.mes_ano_referencia,
    f.valor,
    f.data_vencimento,
    f.situacao,
    f.codigo_boleto,
    f.data_pagamento,
    c.id AS cliente_id,
    c.nome AS nome_cliente,
    c.cpf AS cpf_cliente,
    c.email AS email_cliente,
    c.telefone AS telefone_cliente
FROM
    faturas f
JOIN
    clientes c ON f.cliente_id = c.id;