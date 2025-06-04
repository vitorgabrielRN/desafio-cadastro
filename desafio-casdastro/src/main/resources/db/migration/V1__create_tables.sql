CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    email VARCHAR(255),
    endereco TEXT,
    telefone VARCHAR(20)
);

CREATE TABLE faturas (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER NOT NULL, 
    numero_fatura VARCHAR(50) UNIQUE NOT NULL,
    mes_ano_referencia VARCHAR(7) NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    data_vencimento DATE NOT NULL,
    situacao VARCHAR(20) NOT NULL DEFAULT 'gerada',
    codigo_boleto VARCHAR(255),
    data_pagamento DATE,
    CONSTRAINT fk_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES clientes (id)
);