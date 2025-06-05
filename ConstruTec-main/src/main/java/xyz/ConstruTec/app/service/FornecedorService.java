package xyz.ConstruTec.app.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.ConstruTec.app.dao.FornecedorDao;
import xyz.ConstruTec.app.model.Fornecedor;
import xyz.ConstruTec.app.util.PaginacaoUtil;

@Service
public class FornecedorService {
	
	@Autowired
	private FornecedorDao dao;
	
	@Transactional(readOnly = false)
	public void salvar(Fornecedor fornecedor) {
		dao.save(fornecedor);
	}
	
	@Transactional(readOnly = true)
	public List<Fornecedor> buscarTodos(){
		return dao.findAll();
	}
	
	@Transactional(readOnly = true)
	public Fornecedor buscarPorId(Long id) {
		return dao.findById(id);
	}
	
	@Transactional(readOnly = true)
	public PaginacaoUtil<Fornecedor> buscarPorPagina(int pagina) {
		return dao.buscaPaginada(pagina);
	}
	
	@Transactional(readOnly = false)
	public void excluir(Long id) {
		dao.delete(id);
	}
	
	@Transactional(readOnly = false)
	public void editar(@Valid Fornecedor fornecedor) {
		dao.update(fornecedor);
		
	}
	
	@Transactional(readOnly = true)
	public PaginacaoUtil<Fornecedor> buscarPorPaginaNomeFantasia(String nomeFantasia, int pagina) {
		return dao.findByNomeFantasia(nomeFantasia, pagina);
	}
	
	@Transactional(readOnly = true)
	public PaginacaoUtil<Fornecedor> buscarPorPaginaRazaoSocial(String razaoSocial, int pagina) {
		return dao.findByRazaoSocial(razaoSocial, pagina);
	}
	
	@Transactional(readOnly = true)
	public Fornecedor buscarPorCnpj(String cnpj) {
		Fornecedor fornecedor = dao.findByCnpj(cnpj);
		return fornecedor;
	}
	
	@Transactional(readOnly = true)
	public Fornecedor buscarPorInscricaoEstadual(String inscricaoEstadual) {
		Fornecedor fornecedor = dao.findByInscricaoEstadual(inscricaoEstadual);
		return fornecedor;
	}
	
}
