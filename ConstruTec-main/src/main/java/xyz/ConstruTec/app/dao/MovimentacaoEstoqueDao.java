package xyz.ConstruTec.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xyz.ConstruTec.app.model.MovimentacaoEstoque;
import xyz.ConstruTec.app.model.Obra;

import java.util.List;

@Repository
public interface MovimentacaoEstoqueDao extends JpaRepository<MovimentacaoEstoque, Long> {
    
    @Query("SELECT m FROM MovimentacaoEstoque m WHERE m.origem = :obra OR m.destino = :obra ORDER BY m.dataMovimentacao DESC")
    List<MovimentacaoEstoque> findByObra(Obra obra);
    
    @Query("SELECT m FROM MovimentacaoEstoque m WHERE m.origem IS NULL OR m.destino IS NULL ORDER BY m.dataMovimentacao DESC")
    List<MovimentacaoEstoque> findByMatriz();
} 