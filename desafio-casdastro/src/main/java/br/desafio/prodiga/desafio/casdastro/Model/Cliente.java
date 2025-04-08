package br.desafio.prodiga.desafio.casdastro.Model;

class Cliente {
    private  String nome;
    private  String email;
    private  String telefone;
    private  String cpf;
    private  String endereco;
    private  String senha;


        @Override
    public String toString() {
        return "DadosPessoais [nome=" + nome + ", email=" + email + ", telefone=" + telefone + ", CPF=" + cpf
                + ", endereco=" + endereco + "]";
    }

    public Cliente(String nome, String email, String telefone, String cpf, String endereco){
          
            this.nome = nome;
            this.email = email;
            this.telefone = telefone;
            this.cpf = cpf;
            this.endereco = endereco;

        }  

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCPF() {
        return cpf;
    }

    public void setCPF(String cpf) {
       thi.cpf = cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public static void main(String[] args) {

   

}
}