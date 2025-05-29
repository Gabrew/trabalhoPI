package xyz.ConstruTec.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xyz.ConstruTec.app.model.Estoque;
import xyz.ConstruTec.app.model.Obra;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstoqueDao extends JpaRepository<Estoque, Long> {
    
    @Query("SELECT e FROM Estoque e WHERE e.obra IS NULL")
    List<Estoque> findAllMatriz();
    
    @Query("SELECT e FROM Estoque e WHERE e.obra = :obra")
    List<Estoque> findByObra(Obra obra);
    
    @Query("SELECT e FROM Estoque e WHERE e.obra = :obra AND e.produto.id = :produtoId")
    Optional<Estoque> findByObraAndProdutoId(Obra obra, Long produtoId);
    
    @Query("SELECT e FROM Estoque e WHERE e.obra IS NULL AND e.produto.id = :produtoId")
    Optional<Estoque> findByMatrizAndProdutoId(Long produtoId);
} 