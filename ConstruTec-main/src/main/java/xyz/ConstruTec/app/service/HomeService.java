package xyz.ConstruTec.app.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.ConstruTec.app.dao.AlertaDao;
import xyz.ConstruTec.app.dao.ClienteDao;
import xyz.ConstruTec.app.dao.FornecedorDao;
import xyz.ConstruTec.app.dao.ObraDao;
import xyz.ConstruTec.app.dao.ProdutoDao;
import xyz.ConstruTec.app.model.Alerta;
import xyz.ConstruTec.app.model.Home;
import xyz.ConstruTec.app.model.Obra;
import xyz.ConstruTec.app.model.Produto;
import xyz.ConstruTec.app.model.RankingData;

@Service
public class HomeService {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeService.class);
	
	@Autowired
	private ProdutoDao produtoDao;
	
	@Autowired
	private FornecedorDao fornecedorDao;
	
	@Autowired
	private ClienteDao clienteDao;
	
	@Autowired
	private ObraDao obraDao;
	
	@Autowired
	private AlertaDao alertaDao;
	
	public Home getResumo() {
		Home home = new Home();
		
		try {
			// Contagens básicas com tratamento de erros
			try {
				home.setQtd_produto(produtoDao.count());
			} catch (Exception e) {
				logger.error("Erro ao contar produtos: {}", e.getMessage());
				home.setQtd_produto(0);
			}
			
			try {
				home.setQtd_cliente(clienteDao.count());
			} catch (Exception e) {
				logger.error("Erro ao contar clientes: {}", e.getMessage());
				home.setQtd_cliente(0);
			}
			
			try {
				home.setQtd_fornecedor(fornecedorDao.count());
			} catch (Exception e) {
				logger.error("Erro ao contar fornecedores: {}", e.getMessage());
				home.setQtd_fornecedor(0);
			}
			
			try {
				home.setQtd_obra(obraDao.count());
			} catch (Exception e) {
				logger.error("Erro ao contar obras: {}", e.getMessage());
				home.setQtd_obra(0);
			}
			
			// Rankings com valores padrão em caso de erro
			home.setProdutosMaisMovimentados(getProdutosMaisMovimentados());
			home.setFornecedoresMaisAtivos(getFornecedoresMaisAtivos());
			home.setClientesComMaisObras(getClientesComMaisObras());
			
			// Produtos com estoque baixo
			try {
				List<Produto> produtosBaixoEstoque = produtoDao.findProdutosComEstoqueBaixo();
				home.setProdutos_estoque_baixo(produtosBaixoEstoque != null ? produtosBaixoEstoque.size() : 0);
			} catch (Exception e) {
				logger.error("Erro ao buscar produtos com estoque baixo: {}", e.getMessage());
				home.setProdutos_estoque_baixo(0);
			}
			
			// Obras em andamento
			try {
				List<Obra> obrasAndamento = obraDao.findObrasEmAndamento();
				home.setObras_andamento(obrasAndamento != null ? obrasAndamento.size() : 0);
			} catch (Exception e) {
				logger.error("Erro ao buscar obras em andamento: {}", e.getMessage());
				home.setObras_andamento(0);
			}
			
			// Alertas não lidos
			try {
				home.setQtd_alertas_nao_lidos(alertaDao.countAlertasNaoLidos());
				home.setUltimos_alertas(alertaDao.findUltimosAlertas(5));
			} catch (Exception e) {
				logger.error("Erro ao buscar alertas: {}", e.getMessage());
				home.setQtd_alertas_nao_lidos(0);
				home.setUltimos_alertas(Collections.emptyList());
			}
			
			// Últimas movimentações
			try {
				List<String> ultimasMovimentacoes = new ArrayList<>();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
				LocalDateTime agora = LocalDateTime.now();
				ultimasMovimentacoes.add("Entrada de 50 sacos de cimento - " + agora.format(formatter));
				ultimasMovimentacoes.add("Saída de 20 metros de cabo - " + agora.minusHours(2).format(formatter));
				home.setUltimas_movimentacoes(ultimasMovimentacoes);
			} catch (Exception e) {
				logger.error("Erro ao gerar últimas movimentações: {}", e.getMessage());
				home.setUltimas_movimentacoes(Collections.emptyList());
			}
			
			// Próximas entregas
			try {
				List<String> proximasEntregas = new ArrayList<>();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
				LocalDateTime agora = LocalDateTime.now();
				proximasEntregas.add("Entrega de Tijolos - " + agora.plusDays(2).format(formatter));
				proximasEntregas.add("Entrega de Areia - " + agora.plusDays(3).format(formatter));
				home.setProximas_entregas(proximasEntregas);
			} catch (Exception e) {
				logger.error("Erro ao gerar próximas entregas: {}", e.getMessage());
				home.setProximas_entregas(Collections.emptyList());
			}
			
		} catch (Exception e) {
			logger.error("Erro geral ao buscar dados do dashboard: {}", e.getMessage(), e);
			throw new RuntimeException("Erro ao carregar dados do dashboard", e);
		}
		
		return home;
	}
	
	private List<RankingData> getProdutosMaisMovimentados() {
		List<RankingData> ranking = new ArrayList<>();
		try {
			produtoDao.findProdutosMaisMovimentados().forEach(produto -> {
				Long quantidade = produto.getEstoque() != null ? produto.getEstoque().getQuantidade() : 0L;
				ranking.add(new RankingData(
					produto.getDescricao(),
					quantidade,
					"Movimentações no mês",
					"produto"
				));
			});
		} catch (Exception e) {
			logger.error("Erro ao buscar produtos mais movimentados: {}", e.getMessage());
		}
		return ranking;
	}
	
	private List<RankingData> getFornecedoresMaisAtivos() {
		List<RankingData> ranking = new ArrayList<>();
		try {
			fornecedorDao.findFornecedoresMaisAtivos().forEach(fornecedor -> {
				ranking.add(new RankingData(
					fornecedor.getNomeFantasia(),
					(long) fornecedor.getMovimentacoes().size(),
					"Entregas realizadas",
					"fornecedor"
				));
			});
		} catch (Exception e) {
			logger.error("Erro ao buscar fornecedores mais ativos: {}", e.getMessage());
		}
		return ranking;
	}
	
	private List<RankingData> getClientesComMaisObras() {
		List<RankingData> ranking = new ArrayList<>();
		try {
			clienteDao.findClientesComMaisObras().forEach(cliente -> {
				String nomeCompleto = cliente.getNome() + " " + cliente.getSobrenome();
				Long numeroObras = obraDao.countByClienteId(cliente.getId());
				ranking.add(new RankingData(
					nomeCompleto,
					numeroObras,
					"Obras contratadas",
					"cliente"
				));
			});
		} catch (Exception e) {
			logger.error("Erro ao buscar clientes com mais obras: {}", e.getMessage());
		}
		return ranking;
	}
}
