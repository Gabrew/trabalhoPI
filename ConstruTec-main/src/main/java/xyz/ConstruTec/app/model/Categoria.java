package xyz.ConstruTec.app.model;

public enum Categoria {
	
	ALVENARIA(1, "ALVENARIA"),
	ACABAMENTO(2, "ACABAMENTO"),
	PECA(3, "PEÇA");
	
	private int codigo;
	private String descricao;
	
	private Categoria(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public String getDescricao() {
		return descricao;
	}

}
