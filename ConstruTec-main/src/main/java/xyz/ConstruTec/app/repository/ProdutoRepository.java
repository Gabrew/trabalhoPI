package xyz.ConstruTec.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import xyz.ConstruTec.app.model.Categoria;
import xyz.ConstruTec.app.model.Fornecedor;
import xyz.ConstruTec.app.model.Produto;
import xyz.ConstruTec.app.util.PaginacaoUtil;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p FROM Produto p ORDER BY p.id")
    List<Produto> buscarTodos();

    @Query("SELECT p FROM Produto p WHERE p.id = :codigo ORDER BY p.id")
    List<Produto> buscarPorCodigo(@Param("codigo") Long codigo);

    @Query("SELECT p FROM Produto p WHERE LOWER(p.descricao) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY p.id")
    List<Produto> buscarPorNome(@Param("nome") String nome);

    @Query("SELECT p FROM Produto p WHERE p.categoria = :categoria ORDER BY p.id")
    List<Produto> buscarPorCategoria(@Param("categoria") Categoria categoria);

    @Query("SELECT DISTINCT p FROM Produto p JOIN p.estoques e WHERE e.fornecedor = :fornecedor ORDER BY p.id")
    List<Produto> buscarPorFornecedor(@Param("fornecedor") Fornecedor fornecedor);
} 