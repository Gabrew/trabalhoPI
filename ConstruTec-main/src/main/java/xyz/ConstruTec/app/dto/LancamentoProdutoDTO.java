package xyz.ConstruTec.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

public class LancamentoProdutoDTO {
    
    @NotNull(message = "O ID da obra é obrigatório")
    private Long obraId;
    
    @NotNull(message = "O produto é obrigatório")
    private Long produtoId;
    
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser maior que zero")
    private Integer quantidade;
    
    @NotNull(message = "O fornecedor é obrigatório")
    private Long fornecedorId;
    
    @NotNull(message = "O preço de custo é obrigatório")
    @Min(value = 0, message = "O preço de custo deve ser maior ou igual a zero")
    private BigDecimal precoCusto;
    
    @NotNull(message = "A data é obrigatória")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataRetirada;

    // Getters e Setters
    public Long getObraId() {
        return obraId;
    }

    public void setObraId(Long obraId) {
        this.obraId = obraId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }

    public LocalDate getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(LocalDate dataRetirada) {
        this.dataRetirada = dataRetirada;
    }
} 