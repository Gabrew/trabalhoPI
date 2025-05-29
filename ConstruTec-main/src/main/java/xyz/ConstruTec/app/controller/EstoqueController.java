package xyz.ConstruTec.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import xyz.ConstruTec.app.model.Estoque;
import xyz.ConstruTec.app.model.MovimentacaoEstoque;
import xyz.ConstruTec.app.model.Obra;
import xyz.ConstruTec.app.model.Produto;
import xyz.ConstruTec.app.service.EstoqueService;
import xyz.ConstruTec.app.service.ObraService;
import xyz.ConstruTec.app.service.ProdutoService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private ObraService obraService;

    @Autowired
    private ProdutoService produtoService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @GetMapping
    public String listarEstoqueMatriz(ModelMap model) {
        model.addAttribute("produtos", produtoService.buscarTodos());
        model.addAttribute("estoques", estoqueService.buscarEstoqueMatriz());
        return "estoque/estoque-matriz";
    }

    @GetMapping("/transferencia")
    public String novaTransferencia(ModelMap model) {
        // Busca todos os produtos e obras
        model.addAttribute("produtos", produtoService.buscarTodos());
        model.addAttribute("obras", obraService.buscarTodasObras());
        
        // Mapa de estoque por produto na matriz
        Map<Long, Integer> estoqueProduto = new HashMap<>();
        estoqueService.buscarEstoqueMatriz().forEach(e -> 
            estoqueProduto.put(e.getProduto().getId(), e.getQuantidade())
        );
        model.addAttribute("estoqueProduto", estoqueProduto);
        
        // Mapa de estoque por produto e obra
        Map<Long, Map<Long, Integer>> estoqueObras = new HashMap<>();
        for (Obra obra : obraService.buscarTodasObras()) {
            estoqueService.buscarEstoqueObra(obra).forEach(e -> {
                estoqueObras.computeIfAbsent(e.getProduto().getId(), k -> new HashMap<>())
                           .put(obra.getId(), e.getQuantidade());
            });
        }
        model.addAttribute("estoqueObras", estoqueObras);
        
        // Últimas movimentações
        model.addAttribute("movimentacoes", estoqueService.buscarMovimentacoesRecentes(10));
        
        return "estoque/transferencia";
    }

    @PostMapping("/entrada")
    public String registrarEntrada(
            @RequestParam("produtoId") Long produtoId,
            @RequestParam("quantidade") Integer quantidade,
            @RequestParam("notaFiscal") String notaFiscal,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime dataEntrada,
            @RequestParam(required = false) String observacao,
            RedirectAttributes attr) {
        
        try {
            Produto produto = produtoService.buscarPorId(produtoId);
            if (produto == null) {
                throw new RuntimeException("Produto não encontrado");
            }

            // Cria a movimentação de entrada
            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
            movimentacao.setProduto(produto);
            movimentacao.setQuantidade(quantidade);
            movimentacao.setDataMovimentacao(dataEntrada);
            movimentacao.setObservacao("NF: " + notaFiscal + (observacao != null ? " - " + observacao : ""));

            // Realiza a entrada no estoque
            estoqueService.registrarEntradaMatriz(movimentacao);
            attr.addFlashAttribute("sucesso", "Entrada registrada com sucesso");
            
        } catch (Exception e) {
            attr.addFlashAttribute("erro", "Erro ao registrar entrada: " + e.getMessage());
        }

        return "redirect:/estoque";
    }

    @PostMapping("/transferir")
    public String realizarTransferencia(
            @RequestParam("produtoId") Long produtoId,
            @RequestParam("quantidade") Integer quantidade,
            @RequestParam(value = "origemId", required = false) Long origemId,
            @RequestParam(value = "destinoId", required = false) Long destinoId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime dataMovimentacao,
            @RequestParam(value = "observacao", required = false) String observacao,
            RedirectAttributes attr) {
        
        try {
            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
            
            // Busca e seta o produto
            Produto produto = produtoService.buscarPorId(produtoId);
            if (produto == null) {
                throw new RuntimeException("Produto não encontrado");
            }
            movimentacao.setProduto(produto);
            
            // Seta a quantidade
            movimentacao.setQuantidade(quantidade);
            
            // Busca e seta a origem (se houver)
            if (origemId != null) {
                Obra origem = obraService.buscarPorId(origemId);
                if (origem == null) {
                    throw new RuntimeException("Obra de origem não encontrada");
                }
                movimentacao.setOrigem(origem);
            }
            
            // Busca e seta o destino (se houver)
            if (destinoId != null) {
                Obra destino = obraService.buscarPorId(destinoId);
                if (destino == null) {
                    throw new RuntimeException("Obra de destino não encontrada");
                }
                movimentacao.setDestino(destino);
            }
            
            // Valida origem e destino
            if (movimentacao.getOrigem() == null && movimentacao.getDestino() == null) {
                throw new RuntimeException("É necessário selecionar pelo menos uma origem ou destino");
            }
            
            if (movimentacao.getOrigem() != null && movimentacao.getDestino() != null 
                && movimentacao.getOrigem().getId().equals(movimentacao.getDestino().getId())) {
                throw new RuntimeException("A origem e o destino não podem ser iguais");
            }
            
            // Valida se há estoque suficiente
            if (movimentacao.getOrigem() != null) {
                Estoque estoqueOrigem = estoqueService.buscarEstoqueProdutoObra(produto, movimentacao.getOrigem());
                if (estoqueOrigem == null || estoqueOrigem.getQuantidade() < quantidade) {
                    throw new RuntimeException("Quantidade insuficiente em estoque na origem");
                }
            } else {
                Estoque estoqueMatriz = estoqueService.buscarEstoqueProdutoMatriz(produto);
                if (estoqueMatriz == null || estoqueMatriz.getQuantidade() < quantidade) {
                    throw new RuntimeException("Quantidade insuficiente em estoque na matriz");
                }
            }
            
            // Seta a data e observação
            movimentacao.setDataMovimentacao(dataMovimentacao);
            movimentacao.setObservacao(observacao);
            
            // Realiza a transferência
            estoqueService.realizarTransferencia(movimentacao);
            attr.addFlashAttribute("sucesso", "Transferência realizada com sucesso");
            
        } catch (Exception e) {
            attr.addFlashAttribute("erro", "Erro ao realizar transferência: " + e.getMessage());
        }

        return "redirect:/estoque/transferencia";
    }

    @GetMapping("/historico/{produtoId}")
    public String historicoMovimentacoes(
            @PathVariable Long produtoId,
            @RequestParam(required = false) Long obraId,
            ModelMap model) {
        Produto produto = produtoService.buscarPorId(produtoId);
        if (produto != null) {
            model.addAttribute("produto", produto);
            
            if (obraId != null) {
                Obra obra = obraService.buscarPorId(obraId);
                if (obra != null) {
                    model.addAttribute("obra", obra);
                    model.addAttribute("movimentacoes", estoqueService.buscarMovimentacoesProdutoObra(produto, obra));
                    return "estoque/historico";
                }
            }
            
            model.addAttribute("movimentacoes", estoqueService.buscarMovimentacoesProduto(produto));
            return "estoque/historico";
        }
        return "redirect:/estoque";
    }
} 