package br.desafio.prodiga.Service;

import java.util.List;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Repository.ClienteRepository;
import br.desafio.prodiga.Repository.FaturaRepository;

public class FaturaService {
  
    private ClienteRepository clienteRepositorio;
    private FaturaRepository faturaRepository;




    public void gerarFaturas (int mesReferencia, int anoReferencia){
        List<Cliente> clientesSalvos = clienteRepositorio.listarClientesSalvos();

        for(Cliente cliente : clientesSalvos) {
            Fatura fatura = new Fatura();
            fatura.setAnoReferencia(anoReferencia);
            fatura.setValor(GeradorNumeros.gerarValorAleatorio(10, 100));
        
        }
    }

    public List<Fatura> gerarFaturas(Long clienteId){
        return faturaRepository.buscarPorCliente(clienteId);
    }

}
