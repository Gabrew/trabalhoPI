package xyz.ConstruTec.app.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "MOVIMENTACOES_ESTOQUE")
public class MovimentacaoEstoque extends AbstractEntity<Long> {
    
    @ManyToOne
    @JoinColumn(name = "produto_id")
    @NotNull(message = "O produto é obrigatório")
    private Produto produto;
    
    @ManyToOne
    @JoinColumn(name = "origem_id")
    private Obra origem; // null significa que é da matriz
    
    @ManyToOne
    @JoinColumn(name = "destino_id")
    private Obra destino; // null significa que é para matriz
    
    @NotNull(message = "A quantidade é obrigatória")
    private Integer quantidade;
    
    @Column(name = "data_movimentacao")
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