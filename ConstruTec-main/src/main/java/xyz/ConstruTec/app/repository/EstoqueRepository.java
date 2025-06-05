package xyz.ConstruTec.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.ConstruTec.app.model.Estoque;
import xyz.ConstruTec.app.model.Obra;
import xyz.ConstruTec.app.model.Produto;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    
    @Query("SELECT e FROM Estoque e LEFT JOIN FETCH e.produto WHERE e.obra IS NULL")
    List<Estoque> findByObraIsNull();
    
    @Query("SELECT e FROM Estoque e LEFT JOIN FETCH e.produto WHERE e.obra = :obra")
    List<Estoque> findByObra(@Param("obra") Obra obra);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Estoque e LEFT JOIN FETCH e.produto WHERE e.produto = :produto AND e.obra IS NULL")
    Optional<Estoque> findByProdutoAndObraIsNull(@Param("produto") Produto produto);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Estoque e LEFT JOIN FETCH e.produto WHERE e.produto = :produto AND e.obra = :obra")
    Optional<Estoque> findByProdutoAndObra(@Param("produto") Produto produto, @Param("obra") Obra obra);
} 