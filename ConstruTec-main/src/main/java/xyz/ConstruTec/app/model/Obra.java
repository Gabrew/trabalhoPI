package xyz.ConstruTec.app.model;

import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "obras")
public class Obra extends AbstractEntity<Long> {

    @Column(name = "nome", nullable = false)
    private String nome;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_termino")
    private LocalDate dataTermino;

    @Column(name = "valor_estimado", nullable = false)
    private BigDecimal valorEstimado;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "estado")
    private String estado;

    @Column(name = "cep")
    private String cep;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusObra status = StatusObra.EM_ANDAMENTO;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "obra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RetiradaProduto> retiradaProdutos = new ArrayList<>();

    @OneToMany(mappedBy = "obra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recebimento> recebimentos = new ArrayList<>();

    // Getters e Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public BigDecimal getValorEstimado() {
        return valorEstimado;
    }

    public void setValorEstimado(BigDecimal valorEstimado) {
        this.valorEstimado = valorEstimado;
    }

    public StatusObra getStatus() {
        return status;
    }

    public void setStatus(StatusObra status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<RetiradaProduto> getRetiradaProdutos() {
        return retiradaProdutos;
    }

    public void setRetiradaProdutos(List<RetiradaProduto> retiradaProdutos) {
        this.retiradaProdutos = retiradaProdutos;
    }

    public List<Recebimento> getRecebimentos() {
        return recebimentos;
    }

    public void setRecebimentos(List<Recebimento> recebimentos) {
        this.recebimentos = recebimentos;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    // Métodos auxiliares

    public void adicionarRetiradaProduto(RetiradaProduto retirada) {
        retirada.setObra(this);
        this.retiradaProdutos.add(retirada);
    }

    public void removerRetiradaProduto(RetiradaProduto retirada) {
        retirada.setObra(null);
        this.retiradaProdutos.remove(retirada);
    }

    public void adicionarRecebimento(Recebimento recebimento) {
        recebimento.setObra(this);
        this.recebimentos.add(recebimento);
    }

    public void removerRecebimento(Recebimento recebimento) {
        recebimento.setObra(null);
        this.recebimentos.remove(recebimento);
    }

    public String getEnderecoCompleto() {
        StringBuilder sb = new StringBuilder();
        if (endereco != null && !endereco.trim().isEmpty()) {
            sb.append(endereco.trim());
        }
        if (cidade != null && !cidade.trim().isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(cidade.trim());
        }
        if (estado != null && !estado.trim().isEmpty()) {
            if (sb.length() > 0) sb.append(" - ");
            sb.append(estado.trim());
        }
        if (cep != null && !cep.trim().isEmpty()) {
            if (sb.length() > 0) sb.append(", CEP: ");
            sb.append(cep.trim());
        }
        return sb.toString();
    }
}
