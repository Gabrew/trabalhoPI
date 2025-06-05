package xyz.ConstruTec.app.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.ConstruTec.app.model.Obra;
import xyz.ConstruTec.app.model.StatusObra;

@Repository
public interface ObraDao extends JpaRepository<Obra, Long> {

    @Query("select o from Obra o where o.status = 'EM_ANDAMENTO'")
    List<Obra> findObrasEmAndamento();

    @Query("select count(o) from Obra o where o.cliente.id = :clienteId")
    Long countByClienteId(@Param("clienteId") Long clienteId);

    long count();
}
