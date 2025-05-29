package xyz.ConstruTec.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.ConstruTec.app.model.LancamentoProdutoObra;

import java.util.List;

@Repository
public interface LancamentoProdutoObraDao extends JpaRepository<LancamentoProdutoObra, Long> {
    List<LancamentoProdutoObra> findByObra_Id(Long obraId);
}
