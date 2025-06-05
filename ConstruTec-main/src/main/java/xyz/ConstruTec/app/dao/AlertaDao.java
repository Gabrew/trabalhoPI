package xyz.ConstruTec.app.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import xyz.ConstruTec.app.model.Alerta;

@Repository
public class AlertaDao extends AbstractDao<Alerta, Long> {
    
    public List<Alerta> findAlertasNaoLidos() {
        return getEntityManager()
                .createQuery("select a from Alerta a where a.lido = false order by a.dataHora desc", Alerta.class)
                .getResultList();
    }
    
    public List<Alerta> findUltimosAlertas(int quantidade) {
        return getEntityManager()
                .createQuery("select a from Alerta a order by a.dataHora desc", Alerta.class)
                .setMaxResults(quantidade)
                .getResultList();
    }
    
    public long countAlertasNaoLidos() {
        return getEntityManager()
                .createQuery("select count(a) from Alerta a where a.lido = false", Long.class)
                .getSingleResult();
    }
    
    public void marcarComoLido(Long id) {
        getEntityManager()
                .createQuery("update Alerta a set a.lido = true where a.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
    
    public void marcarTodosComoLidos() {
        getEntityManager()
                .createQuery("update Alerta a set a.lido = true where a.lido = false")
                .executeUpdate();
    }
} 