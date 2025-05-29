package xyz.ConstruTec.app.model;

import javax.persistence.*;

@Entity
@Table(name = "retirada_produto")
public class RetiradaProduto extends AbstractEntity<Long> {

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private int quantidade;

    @ManyToOne(optional = false)
    @JoinColumn(name = "obra_id")
    private Obra obra;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Obra getObra() {
        return obra;
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }
}
