package xyz.ConstruTec.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.ConstruTec.app.dao.ClienteDao;
import xyz.ConstruTec.app.dao.FornecedorDao;
import xyz.ConstruTec.app.dao.ObraDao;
import xyz.ConstruTec.app.dao.ProdutoDao;
import xyz.ConstruTec.app.model.Home;

@Service
public class HomeService {
	
	@Autowired
	private ProdutoDao produtoDao;
	
	@Autowired
	private FornecedorDao fornecedorDao;
	
	@Autowired
	private ClienteDao clienteDao;
	
	@Autowired
	private ObraDao obraDao;
	
	public Home getResumo() {
		Home home = new Home();
		home.setQtd_produto(produtoDao.count());
		home.setQtd_cliente(clienteDao.count());
		home.setQtd_fornecedor(fornecedorDao.count());
		home.setQtd_obra(obraDao.count());
		return home;
	}
	
}
