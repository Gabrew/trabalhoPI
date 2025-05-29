package xyz.ConstruTec.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.ConstruTec.app.model.Obra;

@Repository
public interface ObraDao extends JpaRepository<Obra, Long> {
}
