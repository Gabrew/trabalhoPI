package xyz.ConstruTec.app.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Table(name = "PRODUTOS")
public class Produto extends AbstractEntity<Long>{
	
	@NotBlank(message = "A descrição do produto é obrigatório")
	@Size(max = 255, min = 3, message = "A descrição do produto deve conter entre {min} e {max} caracteres.")
	@Column(nullable = false)
	private String descricao;
	
	@NotNull(message = "Selecione uma categoria relativa ao produto.")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Categoria categoria;
	
	@NotBlank(message = "O nome fabricante do produto é obrigatório")
	@Size(max = 255, min = 3, message = "O nome fabricante do produto deve conter entre {min} e {max} caracteres.")
	@Column(nullable = false)
	private String fabricante;
	
	@Column(columnDefinition = "TEXT")
	private String informacoes;
	
	@Column(nullable = true)
	private String foto;
	
	@Column(name = "ativo", nullable = false)
	private boolean ativo = true;
	
	@Transient
	private MovimentacaoEstoque ultimaMovimentacao;
	
	@Transient
	private Estoque estoque;
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Categoria getCategoria() {
		return categoria;
	}
	
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	public String getFabricante() {
		return fabricante;
	}
	
	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}
	
	public String getInformacoes() {
		return informacoes;
	}
	
	public void setInformacoes(String informacoes) {
		this.informacoes = informacoes;
	}
	
	public String getFoto() {
		return foto;
	}
	
	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	public boolean isAtivo() {
		return ativo;
	}
	
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
	public MovimentacaoEstoque getUltimaMovimentacao() {
		return ultimaMovimentacao;
	}
	
	public void setUltimaMovimentacao(MovimentacaoEstoque ultimaMovimentacao) {
		this.ultimaMovimentacao = ultimaMovimentacao;
	}
	
	public Estoque getEstoque() {
		return estoque;
	}
	
	public void setEstoque(Estoque estoque) {
		this.estoque = estoque;
	}
	
	@Override
	public String toString() {
		return "Produto [descricao=" + descricao + ", categoria=" + categoria + ", fabricante=" + fabricante
				+ ", informacoes=" + informacoes + ", foto=" + foto + ", ativo=" + ativo + ", getId()=" + getId() + "]";
	}
}
