package xyz.ConstruTec.app.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

@Entity
@Table(name = "ESTOQUES")
public class Estoque extends AbstractEntity<Long> {
    
    @ManyToOne
    @JoinColumn(name = "produto_id")
    @NotNull(message = "O produto é obrigatório")
    private Produto produto;
    
    @ManyToOne
    @JoinColumn(name = "obra_id")
    private Obra obra; // null significa que é estoque da matriz
    
    @NotNull(message = "A quantidade é obrigatória")
    private Integer quantidade;
    
    @Column(name = "quantidade_minima")
    private Integer quantidadeMinima;
    
    @Column(name = "ultima_movimentacao")
    private LocalDateTime ultimaMovimentacao;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    @Column(name = "preco_custo", columnDefinition = "DECIMAL(10,2)")
    private BigDecimal precoCusto;
    
    public Produto getProduto() {
        return produto;
    }
    
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    
    public Obra getObra() {
        return obra;
    }
    
    public void setObra(Obra obra) {
        this.obra = obra;
    }
    
    public Integer getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
    
    public Integer getQuantidadeMinima() {
        return quantidadeMinima;
    }
    
    public void setQuantidadeMinima(Integer quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }
    
    public LocalDateTime getUltimaMovimentacao() {
        return ultimaMovimentacao;
    }
    
    public void setUltimaMovimentacao(LocalDateTime ultimaMovimentacao) {
        this.ultimaMovimentacao = ultimaMovimentacao;
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
    
    @PrePersist
    @PreUpdate
    private void prePersistUpdate() {
        ultimaMovimentacao = LocalDateTime.now();
    }
} 