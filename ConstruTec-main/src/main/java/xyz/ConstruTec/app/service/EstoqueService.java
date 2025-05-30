package xyz.ConstruTec.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.ConstruTec.app.dao.EstoqueDao;
import xyz.ConstruTec.app.dao.MovimentacaoEstoqueDao;
import xyz.ConstruTec.app.model.Estoque;
import xyz.ConstruTec.app.model.MovimentacaoEstoque;
import xyz.ConstruTec.app.model.Obra;
import xyz.ConstruTec.app.model.Produto;
import xyz.ConstruTec.app.repository.EstoqueRepository;
import xyz.ConstruTec.app.repository.MovimentacaoEstoqueRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoRepository;

    @Autowired
    private EstoqueDao estoqueDao;

    @Autowired
    private MovimentacaoEstoqueDao movimentacaoDao;

    public List<Estoque> buscarEstoqueMatriz() {
        return estoqueRepository.findByObraIsNull();
    }

    public List<Estoque> buscarEstoqueObra(Obra obra) {
        return estoqueRepository.findByObra(obra);
    }

    public Estoque buscarEstoqueProdutoMatriz(Produto produto) {
        return estoqueRepository.findByProdutoAndObraIsNull(produto).orElse(null);
    }

    public Estoque buscarEstoqueProdutoObra(Produto produto, Obra obra) {
        return estoqueRepository.findByProdutoAndObra(produto, obra);
    }

    public List<MovimentacaoEstoque> buscarMovimentacoesRecentes(int limite) {
        return movimentacaoRepository.findAllOrderByDataMovimentacaoDesc(PageRequest.of(0, limite));
    }

    public List<MovimentacaoEstoque> buscarMovimentacoesProduto(Produto produto) {
        return movimentacaoRepository.findByProdutoOrderByDataMovimentacaoDesc(produto);
    }

    public List<MovimentacaoEstoque> buscarMovimentacoesProdutoObra(Produto produto, Obra obra) {
        return movimentacaoRepository.findByProdutoAndObraOrderByDataMovimentacaoDesc(produto, obra);
    }

    public List<MovimentacaoEstoque> buscarMovimentacoesMatriz() {
        return movimentacaoDao.findByMatriz();
    }

    public List<MovimentacaoEstoque> buscarMovimentacoesObra(Obra obra) {
        return movimentacaoDao.findByObra(obra);
    }

    public Estoque buscarPorId(Long id) {
        return estoqueDao.findById(id).orElse(null);
    }

    public MovimentacaoEstoque buscarUltimaMovimentacao(Produto produto) {
        List<MovimentacaoEstoque> movimentacoes = movimentacaoRepository.findByProdutoOrderByDataMovimentacaoDesc(produto);
        return movimentacoes.isEmpty() ? null : movimentacoes.get(0);
    }

    @Transactional
    public void registrarEntradaMatriz(MovimentacaoEstoque movimentacao) {
        // Valida a movimentação
        if (movimentacao.getProduto() == null) {
            throw new RuntimeException("Produto é obrigatório");
        }
        if (movimentacao.getQuantidade() <= 0) {
            throw new RuntimeException("Quantidade deve ser maior que zero");
        }
        if (movimentacao.getFornecedor() == null) {
            throw new RuntimeException("Fornecedor é obrigatório");
        }
        if (movimentacao.getPrecoCusto() == null || movimentacao.getPrecoCusto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Preço de custo é obrigatório e deve ser maior que zero");
        }

        // Busca ou cria o estoque na matriz
        Estoque estoque = buscarEstoqueProdutoMatriz(movimentacao.getProduto());
        if (estoque == null) {
            estoque = new Estoque();
            estoque.setProduto(movimentacao.getProduto());
            estoque.setQuantidade(0);
        }

        // Atualiza a quantidade e a última movimentação
        estoque.setQuantidade(estoque.getQuantidade() + movimentacao.getQuantidade());
        estoque.setUltimaMovimentacao(movimentacao.getDataMovimentacao());
        
        // Atualiza fornecedor e preço de custo
        estoque.setFornecedor(movimentacao.getFornecedor());
        estoque.setPrecoCusto(movimentacao.getPrecoCusto());
        
        // Salva o estoque
        estoqueRepository.save(estoque);

        // Salva a movimentação
        movimentacaoRepository.save(movimentacao);
    }

    @Transactional
    public MovimentacaoEstoque realizarTransferencia(MovimentacaoEstoque movimentacao) {
        // Valida a movimentação
        if (movimentacao.getProduto() == null) {
            throw new RuntimeException("Produto é obrigatório");
        }
        if (movimentacao.getQuantidade() <= 0) {
            throw new RuntimeException("Quantidade deve ser maior que zero");
        }
        if (movimentacao.getOrigem() == null && movimentacao.getDestino() == null) {
            throw new RuntimeException("Origem ou destino é obrigatório");
        }
        if (movimentacao.getFornecedor() == null) {
            throw new RuntimeException("Fornecedor é obrigatório");
        }
        if (movimentacao.getPrecoCusto() == null || movimentacao.getPrecoCusto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Preço de custo é obrigatório e deve ser maior que zero");
        }

        try {
            // Busca ou cria os estoques
            Estoque estoqueOrigem = null;
            Estoque estoqueDestino = null;

            // Se a origem é null, significa que é da matriz
            if (movimentacao.getOrigem() == null) {
                estoqueOrigem = buscarEstoqueProdutoMatriz(movimentacao.getProduto());
                if (estoqueOrigem == null) {
                    throw new RuntimeException("Produto não encontrado no estoque da matriz");
                }
            } else {
                estoqueOrigem = buscarEstoqueProdutoObra(movimentacao.getProduto(), movimentacao.getOrigem());
                if (estoqueOrigem == null) {
                    throw new RuntimeException("Produto não encontrado no estoque da obra de origem");
                }
            }

            // Se o destino é null, significa que é para a matriz
            if (movimentacao.getDestino() == null) {
                estoqueDestino = buscarEstoqueProdutoMatriz(movimentacao.getProduto());
                if (estoqueDestino == null) {
                    estoqueDestino = new Estoque();
                    estoqueDestino.setProduto(movimentacao.getProduto());
                    estoqueDestino.setQuantidade(0);
                }
            } else {
                estoqueDestino = buscarEstoqueProdutoObra(movimentacao.getProduto(), movimentacao.getDestino());
                if (estoqueDestino == null) {
                    estoqueDestino = new Estoque();
                    estoqueDestino.setProduto(movimentacao.getProduto());
                    estoqueDestino.setObra(movimentacao.getDestino());
                    estoqueDestino.setQuantidade(0);
                }
            }

            // Valida se há estoque suficiente
            if (estoqueOrigem.getQuantidade() < movimentacao.getQuantidade()) {
                throw new RuntimeException("Quantidade insuficiente em estoque na origem");
            }

            // Realiza a transferência
            estoqueOrigem.setQuantidade(estoqueOrigem.getQuantidade() - movimentacao.getQuantidade());
            estoqueDestino.setQuantidade(estoqueDestino.getQuantidade() + movimentacao.getQuantidade());

            // Atualiza a data da última movimentação
            LocalDateTime agora = LocalDateTime.now();
            estoqueOrigem.setUltimaMovimentacao(agora);
            estoqueDestino.setUltimaMovimentacao(agora);
            movimentacao.setDataMovimentacao(agora);

            // Atualiza fornecedor e preço de custo no estoque destino
            if (movimentacao.getFornecedor() != null) {
                estoqueDestino.setFornecedor(movimentacao.getFornecedor());
            }
            if (movimentacao.getPrecoCusto() != null) {
                estoqueDestino.setPrecoCusto(movimentacao.getPrecoCusto());
            }

            // Salva os estoques
            estoqueRepository.save(estoqueOrigem);
            estoqueRepository.save(estoqueDestino);

            // Salva e retorna a movimentação
            return movimentacaoRepository.save(movimentacao);
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao realizar transferência: " + e.getMessage(), e);
        }
    }

    public boolean verificarEstoqueDisponivel(Produto produto, int quantidade) {
        Estoque estoque = buscarEstoqueProdutoMatriz(produto);
        return estoque != null && estoque.getQuantidade() >= quantidade;
    }

    public void diminuirEstoqueMatriz(Produto produto, int quantidade) {
        Estoque estoque = buscarEstoqueProdutoMatriz(produto);
        if (estoque == null || estoque.getQuantidade() < quantidade) {
            throw new RuntimeException("Quantidade insuficiente em estoque");
        }
        estoque.setQuantidade(estoque.getQuantidade() - quantidade);
        estoqueRepository.save(estoque);
    }

    @Transactional
    public void atualizarEstoque(Estoque estoque) {
        estoqueRepository.save(estoque);
    }
} 