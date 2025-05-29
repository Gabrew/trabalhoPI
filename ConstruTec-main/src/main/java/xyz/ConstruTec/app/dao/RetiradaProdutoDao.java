package xyz.ConstruTec.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.ConstruTec.app.model.RetiradaProduto;

@Repository
public interface RetiradaProdutoDao extends JpaRepository<RetiradaProduto, Long> {
}
