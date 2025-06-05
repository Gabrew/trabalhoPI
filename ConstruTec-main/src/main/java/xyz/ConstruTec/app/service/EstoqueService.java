package xyz.ConstruTec.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
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

    @Transactional(readOnly = true)
    public Estoque buscarEstoqueProdutoMatriz(Produto produto) {
        return estoqueRepository.findByProdutoAndObraIsNull(produto).orElse(null);
    }

    @Transactional(readOnly = true)
    public Estoque buscarEstoqueProdutoObra(Produto produto, Obra obra) {
        return estoqueRepository.findByProdutoAndObra(produto, obra).orElse(null);
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public MovimentacaoEstoque realizarTransferencia(MovimentacaoEstoque movimentacao) {
        System.out.println("\n=== Iniciando processo de transferência no EstoqueService ===");
        
        try {
            // Validações iniciais
            if (movimentacao == null) {
                throw new IllegalArgumentException("Movimentação não pode ser nula");
            }
            if (movimentacao.getProduto() == null) {
                throw new IllegalArgumentException("Produto é obrigatório");
            }
            if (movimentacao.getQuantidade() == null || movimentacao.getQuantidade() <= 0) {
                throw new IllegalArgumentException("Quantidade deve ser maior que zero");
            }
            if (movimentacao.getDestino() == null) {
                throw new IllegalArgumentException("Destino é obrigatório");
            }
            if (movimentacao.getFornecedor() == null) {
                throw new IllegalArgumentException("Fornecedor é obrigatório");
            }
            if (movimentacao.getPrecoCusto() == null || movimentacao.getPrecoCusto().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Preço de custo é obrigatório e deve ser maior que zero");
            }
            
            // 1. Buscar estoque na matriz
            System.out.println("1. Buscando estoque na matriz para o produto: " + movimentacao.getProduto().getDescricao());
            Estoque estoqueMatriz = estoqueRepository.findByProdutoAndObraIsNull(movimentacao.getProduto())
                .orElseThrow(() -> new IllegalStateException("Estoque não encontrado na matriz"));
            System.out.println("   Estoque encontrado na matriz. Quantidade atual: " + estoqueMatriz.getQuantidade());
                
            // 2. Verificar se há quantidade suficiente
            if (estoqueMatriz.getQuantidade() < movimentacao.getQuantidade()) {
                throw new IllegalStateException("Quantidade insuficiente em estoque na matriz. Disponível: " + 
                    estoqueMatriz.getQuantidade() + ", Solicitado: " + movimentacao.getQuantidade());
            }
            System.out.println("2. Quantidade suficiente verificada");
            
            // 3. Retirar da matriz
            System.out.println("3. Retirando " + movimentacao.getQuantidade() + " unidades da matriz");
            estoqueMatriz.setQuantidade(estoqueMatriz.getQuantidade() - movimentacao.getQuantidade());
            estoqueMatriz = estoqueRepository.save(estoqueMatriz);
            System.out.println("   Novo estoque na matriz: " + estoqueMatriz.getQuantidade());
            
            // 4. Buscar ou criar estoque na obra
            System.out.println("4. Buscando ou criando estoque na obra: " + movimentacao.getDestino().getNome());
            Estoque estoqueObra = estoqueRepository.findByProdutoAndObra(movimentacao.getProduto(), movimentacao.getDestino())
                .orElseGet(() -> {
                    System.out.println("   Criando novo estoque na obra");
                    Estoque novo = new Estoque();
                    novo.setProduto(movimentacao.getProduto());
                    novo.setObra(movimentacao.getDestino());
                    novo.setQuantidade(0);
                    return novo;
                });
                
            // 5. Adicionar na obra
            System.out.println("5. Adicionando " + movimentacao.getQuantidade() + " unidades na obra");
            estoqueObra.setQuantidade(estoqueObra.getQuantidade() + movimentacao.getQuantidade());
            estoqueObra.setFornecedor(movimentacao.getFornecedor());
            estoqueObra.setPrecoCusto(movimentacao.getPrecoCusto());
            estoqueObra.setUltimaMovimentacao(LocalDateTime.now());
            estoqueObra = estoqueRepository.save(estoqueObra);
            System.out.println("   Novo estoque na obra: " + estoqueObra.getQuantidade());
            
            // 6. Registrar a movimentação
            System.out.println("6. Registrando a movimentação");
            movimentacao.setDataMovimentacao(LocalDateTime.now());
            MovimentacaoEstoque movimentacaoSalva = movimentacaoRepository.save(movimentacao);
            
            // 7. Forçar flush para garantir que tudo foi salvo
            estoqueRepository.flush();
            movimentacaoRepository.flush();
            
            System.out.println("=== Transferência concluída com sucesso ===\n");
            return movimentacaoSalva;
            
        } catch (Exception e) {
            System.err.println("ERRO durante a transferência: " + e.getMessage());
            e.printStackTrace();
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
        if (estoque == null) {
            throw new RuntimeException("Estoque não pode ser nulo");
        }
        if (estoque.getQuantidade() == null) {
            estoque.setQuantidade(0);
        }
        if (estoque.getUltimaMovimentacao() == null) {
            estoque.setUltimaMovimentacao(LocalDateTime.now());
        }
        estoqueRepository.save(estoque);
    }
} 