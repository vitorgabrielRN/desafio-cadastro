CREATE TABLE clientes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    endereco VARCHAR(255),
    telefone VARCHAR(20)
);

CREATE TABLE faturas (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    numero_fatura VARCHAR(50) UNIQUE NOT NULL,
    mes_ano_referencia VARCHAR(7) NOT NULL, 
    valor DECIMAL(10, 2) NOT NULL,
    data_vencimento DATE NOT NULL,
    situacao VARCHAR(20) NOT NULL, 
    codigo_boleto VARCHAR(100) UNIQUE,
    data_pagamento DATE,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);