package xyz.ConstruTec.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.ConstruTec.app.dao.ProdutoDao;
import xyz.ConstruTec.app.model.Categoria;
import xyz.ConstruTec.app.model.Fornecedor;
import xyz.ConstruTec.app.model.Produto;
import xyz.ConstruTec.app.repository.ProdutoRepository;
import xyz.ConstruTec.app.util.PaginacaoUtil;

@Service
@Transactional(readOnly = false)
public class ProdutoService {
	
	@Autowired
	private ProdutoDao dao;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Transactional(readOnly = false)
	public void salvar(Produto produto) {
		produto.setAtivo(true); // Novo produto sempre começa ativo
		dao.save(produto);
	}
	
	@Transactional(readOnly = true)
	public List<Produto> buscarTodos() {
		return produtoRepository.findAll();
	}
	
	@Transactional(readOnly = false)
	public void excluir(Long id) {
		dao.delete(id);
	}

	@Transactional(readOnly = true)
	public Produto buscarPorId(Long id) {
		return produtoRepository.findById(id).orElse(null);
	}
	
	@Transactional(readOnly = false)
	public void editar(Produto produto) {
		produtoRepository.save(produto);
	}

	private PaginacaoUtil<Produto> criarPaginacao(List<Produto> produtos, int pagina) {
		int tamanho = 5; // Tamanho da página
		int inicio = (pagina - 1) * tamanho;
		int fim = Math.min(inicio + tamanho, produtos.size());
		
		if (inicio >= produtos.size()) {
			return new PaginacaoUtil<>(tamanho, pagina, 1, List.of(), null);
		}
		
		List<Produto> produtosPaginados = produtos.subList(inicio, fim);
		long totalDePaginas = (produtos.size() + tamanho - 1) / tamanho;
		
		return new PaginacaoUtil<>(tamanho, pagina, totalDePaginas, produtosPaginados, null);
	}

	@Transactional(readOnly = true)
	public PaginacaoUtil<Produto> buscarPorPagina(int pagina) {
		List<Produto> produtos = produtoRepository.buscarTodos();
		return criarPaginacao(produtos, pagina);
	}

	@Transactional(readOnly = true)
	public PaginacaoUtil<Produto> buscarPorPaginaNome(String nome, int pagina) {
		List<Produto> produtos = produtoRepository.buscarPorNome(nome);
		return criarPaginacao(produtos, pagina);
	}
	
	@Transactional(readOnly = true)
	public PaginacaoUtil<Produto> buscarPorPaginaCategoria(Categoria categoria, int pagina) {
		List<Produto> produtos = produtoRepository.buscarPorCategoria(categoria);
		return criarPaginacao(produtos, pagina);
	}
	
	@Transactional(readOnly = true)
	public PaginacaoUtil<Produto> buscarPorPaginaFornecedor(Fornecedor fornecedor, int pagina) {
		if (fornecedor == null) {
			return new PaginacaoUtil<>(5, pagina, 0, List.of(), null);
		}
		List<Produto> produtos = produtoRepository.buscarPorFornecedor(fornecedor);
		return criarPaginacao(produtos, pagina);
	}
	
	@Transactional(readOnly = true)
	public PaginacaoUtil<Produto> buscarPorPaginaReferencia(String referencia, int pagina) {
		return dao.findByReferencia(referencia, pagina);
	}
	
	@Transactional(readOnly = true)
	public PaginacaoUtil<Produto> buscarPorPaginaCodigo(Long codigo, int pagina) {
		List<Produto> produtos = produtoRepository.buscarPorCodigo(codigo);
		return criarPaginacao(produtos, pagina);
	}
	
	public void inativar(Long id) {
		Produto produto = buscarPorId(id);
		if (produto != null) {
			produto.setAtivo(false);
			produtoRepository.save(produto);
		}
	}
	
	public void ativar(Long id) {
		Produto produto = buscarPorId(id);
		if (produto != null) {
			produto.setAtivo(true);
			produtoRepository.save(produto);
		}
	}

}
