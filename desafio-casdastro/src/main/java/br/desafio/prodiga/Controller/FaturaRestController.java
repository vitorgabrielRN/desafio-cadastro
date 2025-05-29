package br.desafio.prodiga.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Repository.FaturaRepository;
import br.desafio.prodiga.Service.FaturaService;
import br.desafio.prodiga.dto.Fatura.DadosCadastroFatura;
import br.desafio.prodiga.dto.Fatura.ListaFatura;


@RestController
@RequestMapping("api/faturas")
public class FaturaRestController {
  @Autowired
  private FaturaService service;
  @Autowired
  private FaturaRepository repository;



  @PostMapping
  public void cadastrar(@RequestBody DadosCadastroFatura dados){
    repository.save(new Fatura(dados));
  }

  @GetMapping
  public Page<ListaFatura> listar(Pageable paginacao){
    return repository
  }


}
