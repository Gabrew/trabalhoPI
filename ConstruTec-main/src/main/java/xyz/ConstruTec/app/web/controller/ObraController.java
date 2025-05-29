package xyz.ConstruTec.app.web.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import xyz.ConstruTec.app.dto.ObraDetalhesDTO;
import xyz.ConstruTec.app.model.Estoque;
import xyz.ConstruTec.app.model.MovimentacaoEstoque;
import xyz.ConstruTec.app.model.Obra;
import xyz.ConstruTec.app.model.Produto;
import xyz.ConstruTec.app.model.RetiradaProduto;
import xyz.ConstruTec.app.model.StatusObra;
import xyz.ConstruTec.app.service.ClienteService;
import xyz.ConstruTec.app.service.EstoqueService;
import xyz.ConstruTec.app.service.ObraService;
import xyz.ConstruTec.app.service.ProdutoService;

@Controller
@RequestMapping("/obras")
public class ObraController {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ObraService obraService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EstoqueService estoqueService;

    @ModelAttribute("produtos")
    public List<Produto> getProdutos() {
        return produtoService.buscarTodos();
    }

    @GetMapping
    public String listarObras(Model model) {
        List<Obra> obras = obraService.buscarTodasObras();
        model.addAttribute("clientes", clienteService.buscarTodos());
        model.addAttribute("obra", new Obra());
        model.addAttribute("obras", obras);
        return "obra/obras";
    }

    @GetMapping("/{id}/detalhes")
    public String detalhesObra(@PathVariable Long id, Model model) {
        Obra obra = obraService.buscarPorId(id);
        if (obra == null) {
            return "redirect:/obras";
        }

        // Garante que o status não seja nulo
        if (obra.getStatus() == null) {
            obra.setStatus(StatusObra.EM_ANDAMENTO);
            obraService.salvar(obra);
        }

        // Busca o estoque da obra
        List<Estoque> estoque = estoqueService.buscarEstoqueObra(obra);
        
        // Busca as movimentações da obra
        List<MovimentacaoEstoque> movimentacoes = estoqueService.buscarMovimentacoesObra(obra);
        
        // Calcula quantidade de produtos com estoque baixo (menos de 10 unidades)
        long produtosBaixoEstoque = estoque.stream()
                .filter(e -> e.getQuantidade() < 10)
                .count();

        model.addAttribute("obra", obra);
        model.addAttribute("estoque", estoque);
        model.addAttribute("movimentacoes", movimentacoes);
        model.addAttribute("produtosBaixoEstoque", produtosBaixoEstoque);

        return "obra/detalhes";
    }

    @GetMapping("/nova")
    public String novaObraForm(Model model) {
        Obra obra = new Obra();
        obra.setStatus(StatusObra.NAO_INICIADA); // Define o status inicial
        model.addAttribute("obra", obra);
        model.addAttribute("clientes", clienteService.buscarTodos());
        return "obra/obras";
    }

    @PostMapping
    public String salvarObra(@ModelAttribute Obra obra) {
        // Garante que uma nova obra tenha um status
        if (obra.getStatus() == null) {
            obra.setStatus(StatusObra.NAO_INICIADA);
        }
        obraService.salvar(obra);
        return "redirect:/obras";
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> excluirObra(@PathVariable Long id) {
        boolean excluido = obraService.excluirObra(id);
        return excluido ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/lancar-produto")
    public String lancarProduto(@RequestParam Long obraId,
                               @RequestParam Long produtoId,
                               @RequestParam int quantidade,
                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataRetirada,
                               RedirectAttributes attr) {
        try {
            // Busca a obra e o produto
            Obra obra = obraService.buscarPorId(obraId);
            Produto produto = produtoService.buscarPorId(produtoId);

            if (obra == null || produto == null) {
                attr.addFlashAttribute("erro", "Obra ou produto não encontrado");
                return "redirect:/obras";
            }

            // Verifica se há estoque suficiente na matriz
            if (!estoqueService.verificarEstoqueDisponivel(produto, quantidade)) {
                attr.addFlashAttribute("erro", "Quantidade insuficiente em estoque");
                return "redirect:/obras";
            }

            // Atualiza o status da obra para em andamento se estiver não iniciada
            if (obra.getStatus() == StatusObra.NAO_INICIADA) {
                obra.setStatus(StatusObra.EM_ANDAMENTO);
                obraService.salvar(obra);
            }

            // Cria a movimentação de estoque
            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
            movimentacao.setProduto(produto);
            movimentacao.setQuantidade(quantidade);
            movimentacao.setDataMovimentacao(dataRetirada.atStartOfDay());
            movimentacao.setDestino(obra); // Define a obra como destino
            movimentacao.setObservacao("Lançamento de produto na obra");

            // Realiza a transferência do estoque
            estoqueService.realizarTransferencia(movimentacao);

            // Cria a retirada
            RetiradaProduto retirada = new RetiradaProduto();
            retirada.setObra(obra);
            retirada.setProduto(produto);
            retirada.setQuantidade(quantidade);
            retirada.setDataRetirada(dataRetirada);

            // Salva a retirada
            obraService.adicionarRetirada(obraId, retirada);

            attr.addFlashAttribute("sucesso", "Produto lançado com sucesso");
            return "redirect:/obras/" + obraId + "/detalhes";
        } catch (Exception e) {
            attr.addFlashAttribute("erro", "Erro ao lançar produto: " + e.getMessage());
            return "redirect:/obras";
        }
    }
}
