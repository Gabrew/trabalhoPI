package xyz.ConstruTec.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.ConstruTec.app.model.Estoque;
import xyz.ConstruTec.app.model.Obra;
import xyz.ConstruTec.app.model.Produto;

import java.util.List;
import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    List<Estoque> findByObraIsNull();
    List<Estoque> findByObra(Obra obra);
    @Query("SELECT e FROM Estoque e WHERE e.produto = :produto AND e.obra IS NULL ORDER BY e.ultimaMovimentacao DESC")
    Optional<Estoque> findByProdutoAndObraIsNull(@Param("produto") Produto produto);
    Estoque findByProdutoAndObra(Produto produto, Obra obra);
} 