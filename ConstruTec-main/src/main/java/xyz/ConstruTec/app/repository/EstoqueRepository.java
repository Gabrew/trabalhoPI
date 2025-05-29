package xyz.ConstruTec.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.ConstruTec.app.model.Estoque;
import xyz.ConstruTec.app.model.Obra;
import xyz.ConstruTec.app.model.Produto;

import java.util.List;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    List<Estoque> findByObraIsNull();
    List<Estoque> findByObra(Obra obra);
    Estoque findByProdutoAndObraIsNull(Produto produto);
    Estoque findByProdutoAndObra(Produto produto, Obra obra);
} 