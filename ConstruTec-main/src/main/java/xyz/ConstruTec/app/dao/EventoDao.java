package xyz.ConstruTec.app.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import xyz.ConstruTec.app.model.Evento;

@Repository
public class EventoDao extends AbstractDao<Evento, Long> {
    
    public List<Evento> findEventosEntreDatas(LocalDateTime inicio, LocalDateTime fim) {
        return getEntityManager()
                .createQuery("select e from Evento e where e.inicio >= :inicio and e.fim <= :fim", Evento.class)
                .setParameter("inicio", inicio)
                .setParameter("fim", fim)
                .getResultList();
    }
    
    public List<Evento> findProximosEventos(int quantidade) {
        return getEntityManager()
                .createQuery("select e from Evento e where e.inicio >= :agora order by e.inicio asc", Evento.class)
                .setParameter("agora", LocalDateTime.now())
                .setMaxResults(quantidade)
                .getResultList();
    }
    
    public List<Evento> findEventosPorTipo(Evento.TipoEvento tipo) {
        return getEntityManager()
                .createQuery("select e from Evento e where e.tipo = :tipo order by e.inicio asc", Evento.class)
                .setParameter("tipo", tipo)
                .getResultList();
    }
} 