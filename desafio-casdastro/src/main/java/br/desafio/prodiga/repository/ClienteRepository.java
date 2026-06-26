package br.desafio.prodiga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.desafio.prodiga.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
    

}
