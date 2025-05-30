CREATE TABLE fatura (
    id BIGSERIAL PRIMARY KEY,
    numero_fatura VARCHAR(20) UNIQUE NOT NULL,
    mes INTEGER NOT NULL,
    ano INTEGER NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    data_vencimento DATE NOT NULL,
    codigo_boleto VARCHAR(20) UNIQUE NOT NULL,
    situacao VARCHAR(20) NOT NULL,
    data_pagamento TIMESTAMP,
    data_geracao TIMESTAMP NOT NULL,
    cliente_id BIGINT NOT NULL,
    CONSTRAINT fk_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE INDEX idx_fatura_cliente ON fatura(cliente_id);