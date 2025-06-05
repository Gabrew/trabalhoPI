package xyz.ConstruTec.app.model;

public class RankingData {
    private String nome;
    private Long quantidade;
    private String descricao;
    private String tipo;

    public RankingData(String nome, Long quantidade, String descricao, String tipo) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
} 