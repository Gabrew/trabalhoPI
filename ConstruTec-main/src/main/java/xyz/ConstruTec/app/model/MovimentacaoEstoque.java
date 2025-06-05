package xyz.ConstruTec.app.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

@Entity
@Table(name = "movimentacoes_estoque")
public class MovimentacaoEstoque extends AbstractEntity<Long> {
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "produto_id")
    @NotNull(message = "O produto é obrigatório")
    private Produto produto;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "origem_id")
    private Obra origem; // null significa que é da matriz
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destino_id")
    private Obra destino; // null significa que é para matriz
    
    @NotNull(message = "A quantidade é obrigatória")
    @Positive(message = "A quantidade deve ser maior que zero")
    @Column(nullable = false)
    private Integer quantidade;
    
    @Column(name = "data_movimentacao", nullable = false)
    private LocalDateTime dataMovimentacao;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;
    
    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    @Column(name = "preco_custo", precision = 10, scale = 2, nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal precoCusto;
    
    @Column(name = "observacao")
    private String observacao;
    
    @PrePersist
    private void prePersist() {
        if (dataMovimentacao == null) {
            dataMovimentacao = LocalDateTime.now();
        }
        if (precoCusto == null) {
            precoCusto = BigDecimal.ZERO;
        }
        if (quantidade == null) {
            quantidade = 0;
        }
    }
    
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
    
    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }
    
    public void setDataMovimentacao(LocalDateTime dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }
    
    public Fornecedor getFornecedor() {
        return fornecedor;
    }
    
    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
    
    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }
    
    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }
    
    public String getObservacao() {
        return observacao;
    }
    
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
} 