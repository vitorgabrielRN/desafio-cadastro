package br.desafio.prodiga.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.desafio.prodiga.Model.Cliente;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{

}
