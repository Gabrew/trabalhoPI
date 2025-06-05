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
@Table(name = "alertas")
public class Alerta {
    
    public enum TipoAlerta {
        ESTOQUE_BAIXO,
        OBRA_ATRASADA,
        ENTREGA_PROXIMA,
        PAGAMENTO_PENDENTE
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titulo;
    
    @Column(nullable = false)
    private String mensagem;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAlerta tipo;
    
    @Column(nullable = false)
    private LocalDateTime dataHora;
    
    @Column(nullable = false)
    private boolean lido;
    
    public Alerta() {
        this.dataHora = LocalDateTime.now();
        this.lido = false;
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
    
    public String getMensagem() {
        return mensagem;
    }
    
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    public TipoAlerta getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoAlerta tipo) {
        this.tipo = tipo;
    }
    
    public LocalDateTime getDataHora() {
        return dataHora;
    }
    
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
    
    public boolean isLido() {
        return lido;
    }
    
    public void setLido(boolean lido) {
        this.lido = lido;
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
        Alerta other = (Alerta) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
} 