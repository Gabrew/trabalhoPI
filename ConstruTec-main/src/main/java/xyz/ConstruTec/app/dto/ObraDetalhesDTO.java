package xyz.ConstruTec.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ObraDetalhesDTO {

    private Long id;
    private ClienteDTO cliente;
    private LocalDate dataInicio;
    private LocalDate dataTermino;
    private List<RetiradaDTO> retiradas;
    private List<RecebimentoDTO> recebimentos;
    private BigDecimal custoTotal;

    // Getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(LocalDate dataTermino) {
        this.dataTermino = dataTermino;
    }

    public List<RetiradaDTO> getRetiradas() {
        return retiradas;
    }

    public void setRetiradas(List<RetiradaDTO> retiradas) {
        this.retiradas = retiradas;
    }

    public List<RecebimentoDTO> getRecebimentos() {
        return recebimentos;
    }

    public void setRecebimentos(List<RecebimentoDTO> recebimentos) {
        this.recebimentos = recebimentos;
    }

    public BigDecimal getCustoTotal() {
        return custoTotal;
    }

    public void setCustoTotal(BigDecimal custoTotal) {
        this.custoTotal = custoTotal;
    }

    // Classes internas para ClienteDTO, EnderecoDTO, RetiradaDTO, ProdutoDTO, RecebimentoDTO

    public static class ClienteDTO {
        private Long id;
        private String nomeCompleto;
        private EnderecoDTO endereco;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getNomeCompleto() { return nomeCompleto; }
        public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

        public EnderecoDTO getEndereco() { return endereco; }
        public void setEndereco(EnderecoDTO endereco) { this.endereco = endereco; }
    }

    public static class EnderecoDTO {
        private String logradouro;
        private Integer numero;
        private String bairro;
        private String cidade;
        private String uf;

        public String getLogradouro() { return logradouro; }
        public void setLogradouro(String logradouro) { this.logradouro = logradouro; }

        public Integer getNumero() { return numero; }
        public void setNumero(Integer numero) { this.numero = numero; }

        public String getBairro() { return bairro; }
        public void setBairro(String bairro) { this.bairro = bairro; }

        public String getCidade() { return cidade; }
        public void setCidade(String cidade) { this.cidade = cidade; }

        public String getUf() { return uf; }
        public void setUf(String uf) { this.uf = uf; }
    }

    public static class RetiradaDTO {
        private Long id;
        private String descricao;
        private Integer quantidade;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }

        public Integer getQuantidade() { return quantidade; }
        public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    }

    public static class RecebimentoDTO {
        private Long id;
        private java.math.BigDecimal valor;
        private LocalDate data;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public java.math.BigDecimal getValor() { return valor; }
        public void setValor(java.math.BigDecimal valor) { this.valor = valor; }

        public LocalDate getData() { return data; }
        public void setData(LocalDate data) { this.data = data; }
    }
}
