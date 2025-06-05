package xyz.ConstruTec.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.ConstruTec.app.dao.EventoDao;
import xyz.ConstruTec.app.model.Evento;
import xyz.ConstruTec.app.model.Evento.TipoEvento;

@Service
@Transactional
public class EventoService {
    
    @Autowired
    private EventoDao eventoDao;
    
    public Evento criarEvento(String titulo, String descricao, TipoEvento tipo, LocalDateTime inicio, LocalDateTime fim) {
        Evento evento = new Evento();
        evento.setTitulo(titulo);
        evento.setDescricao(descricao);
        evento.setTipo(tipo);
        evento.setInicio(inicio);
        evento.setFim(fim);
        
        // Definir cor baseada no tipo
        switch (tipo) {
            case ENTREGA:
                evento.setCor("#28a745"); // Verde
                break;
            case REUNIAO:
                evento.setCor("#007bff"); // Azul
                break;
            case INSPECAO:
                evento.setCor("#ffc107"); // Amarelo
                break;
            case PAGAMENTO:
                evento.setCor("#dc3545"); // Vermelho
                break;
            default:
                evento.setCor("#6c757d"); // Cinza
        }
        
        return eventoDao.save(evento);
    }
    
    public List<Evento> buscarEventosEntreDatas(LocalDateTime inicio, LocalDateTime fim) {
        return eventoDao.findEventosEntreDatas(inicio, fim);
    }
    
    public List<Evento> buscarProximosEventos(int quantidade) {
        return eventoDao.findProximosEventos(quantidade);
    }
    
    public List<Evento> buscarEventosPorTipo(TipoEvento tipo) {
        return eventoDao.findEventosPorTipo(tipo);
    }
    
    public void atualizarEvento(Long id, String titulo, String descricao, TipoEvento tipo, LocalDateTime inicio, LocalDateTime fim) {
        Evento evento = eventoDao.findById(id);
        if (evento != null) {
            evento.setTitulo(titulo);
            evento.setDescricao(descricao);
            evento.setTipo(tipo);
            evento.setInicio(inicio);
            evento.setFim(fim);
            eventoDao.update(evento);
        }
    }
    
    public void excluirEvento(Long id) {
        eventoDao.delete(id);
    }
} 