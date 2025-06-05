package xyz.ConstruTec.app.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "eventos")
public class Evento {
    
    public enum TipoEvento {
        ENTREGA,
        REUNIAO,
        INSPECAO,
        PAGAMENTO,
        OUTRO
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titulo;
    
    @Column(nullable = false)
    private String descricao;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEvento tipo;
    
    @Column(nullable = false)
    private LocalDateTime inicio;
    
    @Column(nullable = false)
    private LocalDateTime fim;
    
    @Column(nullable = false)
    private String cor;
    
    public Evento() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public TipoEvento getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoEvento tipo) {
        this.tipo = tipo;
    }
    
    public LocalDateTime getInicio() {
        return inicio;
    }
    
    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }
    
    public LocalDateTime getFim() {
        return fim;
    }
    
    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }
    
    public String getCor() {
        return cor;
    }
    
    public void setCor(String cor) {
        this.cor = cor;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Evento other = (Evento) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
} 