package xyz.ConstruTec.app.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import xyz.ConstruTec.app.model.Fornecedor;
import xyz.ConstruTec.app.util.PaginacaoUtil;

@Repository
public class FornecedorDao extends AbstractDao<Fornecedor, Long>{
	
	public PaginacaoUtil<Fornecedor> buscaPaginada(int pagina) {
		int tamanho = 5;
		int inicio = (pagina - 1) * tamanho;
		List<Fornecedor> produtos = getEntityManager().createQuery("select f from Fornecedor f order by f.nomeFantasia asc", Fornecedor.class)
				.setFirstResult(inicio)
				.setMaxResults(tamanho)
				.getResultList();
		
		long totalRegistros = count();
		long totalDePaginas = (totalRegistros + (tamanho - 1)) / tamanho;
		
		return new PaginacaoUtil<>(tamanho, pagina, totalDePaginas, produtos, null);
	}
	
	public long count() {
		return getEntityManager().createQuery("select count(*) from Fornecedor", Long.class).getSingleResult();
	}
	
	
public PaginacaoUtil<Fornecedor> findByNomeFantasia(String nomeFantasia, int pagina) {
		
		int tamanho = 5;
		int inicio = (pagina - 1) * tamanho;
		List<Fornecedor> fornecedores = getEntityManager().createQuery("select f from Fornecedor f where f.nomeFantasia like concat('%',?1,'%')", Fornecedor.class)
				.setParameter(1, nomeFantasia)
				.setFirstResult(inicio)
				.setMaxResults(tamanho)
				.getResultList();
		
		long totalRegistros = fornecedores.size();
		long totalDePaginas = (totalRegistros + (tamanho - 1)) / tamanho;
		
		return new PaginacaoUtil<>(tamanho, pagina, totalDePaginas, fornecedores, null);
		
}

public PaginacaoUtil<Fornecedor> findByRazaoSocial(String razaoSocial, int pagina) {
	
	int tamanho = 5;
	int inicio = (pagina - 1) * tamanho;
	List<Fornecedor> fornecedores = getEntityManager().createQuery("select f from Fornecedor f where f.razaoSocial like concat('%',?1,'%')", Fornecedor.class)
			.setParameter(1, razaoSocial)
			.setFirstResult(inicio)
			.setMaxResults(tamanho)
			.getResultList();
	
	long totalRegistros = fornecedores.size();
	long totalDePaginas = (totalRegistros + (tamanho - 1)) / tamanho;
	
	return new PaginacaoUtil<>(tamanho, pagina, totalDePaginas, fornecedores, null);
	
}

public Fornecedor findByCnpj(String cnpj) {
	List<Fornecedor> fornecedores = createQuery("select f from Fornecedor f where f.cnpj = ?1", cnpj);
	if (fornecedores.size() > 0) {
		return fornecedores.get(0);
	}
	return null;
}

public Fornecedor findByInscricaoEstadual(String inscricaoEstadual) {
	List<Fornecedor> fornecedores = createQuery("select f from Fornecedor f where f.inscricaoEstadual = ?1", inscricaoEstadual);
	if (fornecedores.size() > 0) {
		return fornecedores.get(0);
	}
	return null;
}

public PaginacaoUtil<Fornecedor> findByNome(String nome, int pagina) {
	int tamanho = 5;
	int inicio = (pagina - 1) * tamanho;
	List<Fornecedor> fornecedores = getEntityManager()
			.createQuery("select f from Fornecedor f where f.nome like concat('%',?1,'%')", Fornecedor.class)
			.setParameter(1, nome).setFirstResult(inicio).setMaxResults(tamanho).getResultList();

	long totalRegistros = fornecedores.size();
	long totalDePaginas = (totalRegistros + (tamanho - 1)) / tamanho;

	return new PaginacaoUtil<>(tamanho, pagina, totalDePaginas, fornecedores, null);
}

public List<Fornecedor> findFornecedoresMaisAtivos() {
	return getEntityManager()
			.createQuery("select f from Fornecedor f " +
					"join f.estoques e " +
					"group by f " +
					"order by count(e) desc", Fornecedor.class)
			.setMaxResults(5)
			.getResultList();
}

}


