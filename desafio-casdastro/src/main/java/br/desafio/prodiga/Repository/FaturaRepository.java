package br.desafio.prodiga.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.dto.Fatura.Situacao;
@Repository
public interface FaturaRepository extends JpaRepository<Fatura, Long> {
    List<Fatura> findByClienteId(Long clienteId);
    List<Fatura> findBySituacao(Situacao situacao);
}