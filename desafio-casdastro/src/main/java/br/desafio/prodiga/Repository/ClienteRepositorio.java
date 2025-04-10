package br.desafio.prodiga.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.desafio.prodiga.Model.Cliente;



public interface ClienteRepositorio extends JpaRepository<Cliente, Long>{

}
