package br.desafio.prodiga.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;

public interface FaturaRepository extends JpaRepository<Fatura, Long> {
    List<Fatura> findBySituacao(String situacao);
    List<Fatura> findByCliente(Cliente cliente);
}