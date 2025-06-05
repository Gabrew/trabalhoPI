package xyz.ConstruTec.app.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import xyz.ConstruTec.app.model.Cliente;
import xyz.ConstruTec.app.util.PaginacaoUtil;

@Repository
public class ClienteDao extends AbstractDao<Cliente, Long> {
	
	public PaginacaoUtil<Cliente> buscaPaginada(int pagina) {
		int tamanho = 5;
		int inicio = (pagina - 1) * tamanho;
		List<Cliente> clientes = getEntityManager().createQuery("select c from Cliente c order by c.nome asc", Cliente.class)
				.setFirstResult(inicio)
				.setMaxResults(tamanho)
				.getResultList();
		
		long totalRegistros = count();
		long totalDePaginas = (totalRegistros + (tamanho - 1)) / tamanho;
		
		return new PaginacaoUtil<>(tamanho, pagina, totalDePaginas, clientes, null);
	}
	
	public long count() {
		return getEntityManager().createQuery("select count(*) from Cliente", Long.class).getSingleResult();
	}
	
	public Cliente findByCpf(String cpf) {
		List<Cliente> clientes = createQuery("select c from Cliente c where c.cpf = ?1", cpf);
		if (clientes.size() > 0) {
			return clientes.get(0);
		}
		return null;
	}
	
	public Cliente findByRg(String rg) {
		List<Cliente> clientes = createQuery("select c from Cliente c where c.rg = ?1", rg);
		if (clientes.size() > 0) {
			return clientes.get(0);
		}
		return null;
	}
	
	public PaginacaoUtil<Cliente> findByNome(String nome, int pagina) {
		int tamanho = 5;
		int inicio = (pagina - 1) * tamanho;
		List<Cliente> clientes = getEntityManager()
				.createQuery("select c from Cliente c where c.nome like concat('%',?1,'%')", Cliente.class)
				.setParameter(1, nome).setFirstResult(inicio).setMaxResults(tamanho).getResultList();
		
		long totalRegistros = clientes.size();
		long totalDePaginas = (totalRegistros + (tamanho - 1)) / tamanho;
		
		return new PaginacaoUtil<>(tamanho, pagina, totalDePaginas, clientes, null);
	}
	
	public PaginacaoUtil<Cliente> findByCpf(String cpf, int pagina) {
		int tamanho = 5;
		int inicio = (pagina - 1) * tamanho;
		List<Cliente> clientes = getEntityManager().createQuery("select c from Cliente c where c.cpf = ?1", Cliente.class)
				.setParameter(1, cpf)
				.setFirstResult(inicio)
				.setMaxResults(tamanho)
				.getResultList();
		
		long totalRegistros = clientes.size();
		long totalDePaginas = (totalRegistros + (tamanho - 1)) / tamanho;
		
		return new PaginacaoUtil<>(tamanho, pagina, totalDePaginas, clientes, null);
	}
	
	public List<Cliente> findClientesComMaisObras() {
		return getEntityManager()
				.createQuery("select c from Cliente c " +
						"join c.obras o " +
						"group by c " +
						"order by count(o) desc", Cliente.class)
				.setMaxResults(5)
				.getResultList();
	}

}
