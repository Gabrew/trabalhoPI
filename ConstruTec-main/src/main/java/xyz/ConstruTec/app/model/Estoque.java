package xyz.ConstruTec.app.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    
    @PrePersist
    @PreUpdate
    private void prePersistUpdate() {
        ultimaMovimentacao = LocalDateTime.now();
    }
} 