package xyz.ConstruTec.app.model;

import java.io.Serializable;
import java.util.List;

public class Home implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long qtd_produto;
	private long qtd_cliente;
	private long qtd_fornecedor;
	private long qtd_obra;
	
	// Novos campos para o dashboard
	private long produtos_estoque_baixo;
	private long obras_andamento;
	private List<String> ultimas_movimentacoes;
	private List<String> proximas_entregas;
	
	// Rankings
	private List<RankingData> produtosMaisMovimentados;
	private List<RankingData> fornecedoresMaisAtivos;
	private List<RankingData> clientesComMaisObras;
	
	// Alertas
	private long qtd_alertas_nao_lidos;
	private List<Alerta> ultimos_alertas;
	
	public long getQtd_produto() {
		return qtd_produto;
	}
	public void setQtd_produto(long qtd_produto) {
		this.qtd_produto = qtd_produto;
	}
	public long getQtd_cliente() {
		return qtd_cliente;
	}
	public void setQtd_cliente(long qtd_cliente) {
		this.qtd_cliente = qtd_cliente;
	}
	public long getQtd_fornecedor() {
		return qtd_fornecedor;
	}
	public void setQtd_fornecedor(long qtd_fornecedor) {
		this.qtd_fornecedor = qtd_fornecedor;
	}
	public long getQtd_obra() {
		return qtd_obra;
	}
	public void setQtd_obra(long qtd_obra) {
		this.qtd_obra = qtd_obra;
	}
	
	// Getters e Setters para os novos campos
	public long getProdutos_estoque_baixo() {
		return produtos_estoque_baixo;
	}
	public void setProdutos_estoque_baixo(long produtos_estoque_baixo) {
		this.produtos_estoque_baixo = produtos_estoque_baixo;
	}
	public long getObras_andamento() {
		return obras_andamento;
	}
	public void setObras_andamento(long obras_andamento) {
		this.obras_andamento = obras_andamento;
	}
	public List<String> getUltimas_movimentacoes() {
		return ultimas_movimentacoes;
	}
	public void setUltimas_movimentacoes(List<String> ultimas_movimentacoes) {
		this.ultimas_movimentacoes = ultimas_movimentacoes;
	}
	public List<String> getProximas_entregas() {
		return proximas_entregas;
	}
	public void setProximas_entregas(List<String> proximas_entregas) {
		this.proximas_entregas = proximas_entregas;
	}
	
	public List<RankingData> getProdutosMaisMovimentados() {
		return produtosMaisMovimentados;
	}
	public void setProdutosMaisMovimentados(List<RankingData> produtosMaisMovimentados) {
		this.produtosMaisMovimentados = produtosMaisMovimentados;
	}
	public List<RankingData> getFornecedoresMaisAtivos() {
		return fornecedoresMaisAtivos;
	}
	public void setFornecedoresMaisAtivos(List<RankingData> fornecedoresMaisAtivos) {
		this.fornecedoresMaisAtivos = fornecedoresMaisAtivos;
	}
	public List<RankingData> getClientesComMaisObras() {
		return clientesComMaisObras;
	}
	public void setClientesComMaisObras(List<RankingData> clientesComMaisObras) {
		this.clientesComMaisObras = clientesComMaisObras;
	}
	
	public long getQtd_alertas_nao_lidos() {
		return qtd_alertas_nao_lidos;
	}
	public void setQtd_alertas_nao_lidos(long qtd_alertas_nao_lidos) {
		this.qtd_alertas_nao_lidos = qtd_alertas_nao_lidos;
	}
	public List<Alerta> getUltimos_alertas() {
		return ultimos_alertas;
	}
	public void setUltimos_alertas(List<Alerta> ultimos_alertas) {
		this.ultimos_alertas = ultimos_alertas;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (qtd_cliente ^ (qtd_cliente >>> 32));
		result = prime * result + (int) (qtd_fornecedor ^ (qtd_fornecedor >>> 32));
		result = prime * result + (int) (qtd_produto ^ (qtd_produto >>> 32));
		result = prime * result + (int) (qtd_obra ^ (qtd_obra >>> 32));
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
		Home other = (Home) obj;
		if (qtd_cliente != other.qtd_cliente)
			return false;
		if (qtd_fornecedor != other.qtd_fornecedor)
			return false;
		if (qtd_produto != other.qtd_produto)
			return false;
		if (qtd_obra != other.qtd_obra)
			return false;
		return true;
	}
	
}
