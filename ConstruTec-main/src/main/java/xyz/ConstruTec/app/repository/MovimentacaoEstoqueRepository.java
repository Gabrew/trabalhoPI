package xyz.ConstruTec.app.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.ConstruTec.app.model.MovimentacaoEstoque;
import xyz.ConstruTec.app.model.Obra;
import xyz.ConstruTec.app.model.Produto;

import java.util.List;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {
    @Query("SELECT m FROM MovimentacaoEstoque m ORDER BY m.dataMovimentacao DESC")
    List<MovimentacaoEstoque> findAllOrderByDataMovimentacaoDesc(Pageable pageable);
    
    List<MovimentacaoEstoque> findByProdutoOrderByDataMovimentacaoDesc(Produto produto);
    
    @Query("SELECT m FROM MovimentacaoEstoque m WHERE m.produto = :produto AND (m.origem = :obra OR m.destino = :obra) ORDER BY m.dataMovimentacao DESC")
    List<MovimentacaoEstoque> findByProdutoAndObraOrderByDataMovimentacaoDesc(@Param("produto") Produto produto, @Param("obra") Obra obra);
} 