package xyz.ConstruTec.app.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

@Entity
@Table(name = "MOVIMENTACOES_ESTOQUE")
public class MovimentacaoEstoque extends AbstractEntity<Long> {
    
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    @NotNull(message = "O produto é obrigatório")
    private Produto produto;
    
    @ManyToOne
    @JoinColumn(name = "origem_id")
    private Obra origem; // null significa que é da matriz
    
    @ManyToOne
    @JoinColumn(name = "destino_id")
    private Obra destino; // null significa que é para matriz
    
    @NotNull(message = "A quantidade é obrigatória")
    @Column(nullable = false)
    private Integer quantidade;
    
    @NotNull(message = "Informe o preço de custo.")
    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    @Column(name = "preco_custo", nullable = false, columnDefinition = "DECIMAL(7,2) DEFAULT 0.00")
    private BigDecimal precoCusto;
    
    @NotNull(message = "Selecione um fornecedor.")
    @ManyToOne
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;
    
    @Column(name = "data_movimentacao", nullable = false)
    private LocalDateTime dataMovimentacao;
    
    @Column(name = "observacao")
    private String observacao;
    
    public Produto getProduto() {
        return produto;
    }
    
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    
    public Obra getOrigem() {
        return origem;
    }
    
    public void setOrigem(Obra origem) {
        this.origem = origem;
    }
    
    public Obra getDestino() {
        return destino;
    }
    
    public void setDestino(Obra destino) {
        this.destino = destino;
    }
    
    public Integer getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
    
    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }
    
    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }
    
    public Fornecedor getFornecedor() {
        return fornecedor;
    }
    
    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
    
    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }
    
    public void setDataMovimentacao(LocalDateTime dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }
    
    public String getObservacao() {
        return observacao;
    }
    
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    
    @PrePersist
    private void prePersist() {
        dataMovimentacao = LocalDateTime.now();
    }
} 