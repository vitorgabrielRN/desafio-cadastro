package br.desafio.prodiga.util;

import java.time.LocalDate;
import java.util.Random;

public class GeradorAleatorioUtils {
  
  
    private static final Random Random = new Random();

    public static Double gerarValorAleatorio(){
        return 10.0 + (90.0 * Random.nextDouble());
    }
     public static LocalDate gerarDataVencimentoAleatoria() {
        return LocalDate.now().plusDays(30 + Random.nextInt(61)); 
    }
    

}
