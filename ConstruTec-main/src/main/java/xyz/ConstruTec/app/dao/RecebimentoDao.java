package xyz.ConstruTec.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.ConstruTec.app.model.Recebimento;

@Repository
public interface RecebimentoDao extends JpaRepository<Recebimento, Long> {
}
