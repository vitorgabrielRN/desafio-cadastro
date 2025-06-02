package br.desafio.prodiga.Service;


import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class BancoAPIService {
    public String registrarBoleto(String numeroFatura, LocalDate dataVencimento, Double valor) {

        return "BOL" + UUID.randomUUID().toString().substring(0, 15).toUpperCase();
    }
}