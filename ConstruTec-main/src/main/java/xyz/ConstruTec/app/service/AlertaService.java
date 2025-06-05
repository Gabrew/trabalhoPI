package xyz.ConstruTec.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.ConstruTec.app.dao.AlertaDao;
import xyz.ConstruTec.app.model.Alerta;
import xyz.ConstruTec.app.model.Alerta.TipoAlerta;

@Service
@Transactional
public class AlertaService {
    
    @Autowired
    private AlertaDao alertaDao;
    
    public void criarAlerta(String titulo, String mensagem, TipoAlerta tipo) {
        Alerta alerta = new Alerta();
        alerta.setTitulo(titulo);
        alerta.setMensagem(mensagem);
        alerta.setTipo(tipo);
        alertaDao.save(alerta);
    }
    
    public List<Alerta> buscarAlertasNaoLidos() {
        return alertaDao.findAlertasNaoLidos();
    }
    
    public List<Alerta> buscarUltimosAlertas(int quantidade) {
        return alertaDao.findUltimosAlertas(quantidade);
    }
    
    public long contarAlertasNaoLidos() {
        return alertaDao.countAlertasNaoLidos();
    }
    
    public void marcarComoLido(Long id) {
        alertaDao.marcarComoLido(id);
    }
    
    public void marcarTodosComoLidos() {
        alertaDao.marcarTodosComoLidos();
    }
    
    public void criarAlertaEstoqueBaixo(String produto, int quantidade) {
        String titulo = "Estoque Baixo - " + produto;
        String mensagem = "O produto " + produto + " está com estoque baixo (" + quantidade + " unidades)";
        criarAlerta(titulo, mensagem, TipoAlerta.ESTOQUE_BAIXO);
    }
    
    public void criarAlertaObraAtrasada(String obra, int diasAtraso) {
        String titulo = "Obra Atrasada - " + obra;
        String mensagem = "A obra " + obra + " está atrasada em " + diasAtraso + " dias";
        criarAlerta(titulo, mensagem, TipoAlerta.OBRA_ATRASADA);
    }
    
    public void criarAlertaEntregaProxima(String produto, String fornecedor, String dataEntrega) {
        String titulo = "Entrega Próxima - " + produto;
        String mensagem = "Entrega de " + produto + " do fornecedor " + fornecedor + " prevista para " + dataEntrega;
        criarAlerta(titulo, mensagem, TipoAlerta.ENTREGA_PROXIMA);
    }
    
    public void criarAlertaPagamentoPendente(String descricao, String valor, String dataVencimento) {
        String titulo = "Pagamento Pendente";
        String mensagem = descricao + " - Valor: R$ " + valor + " - Vencimento: " + dataVencimento;
        criarAlerta(titulo, mensagem, TipoAlerta.PAGAMENTO_PENDENTE);
    }
} 