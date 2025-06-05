package xyz.ConstruTec.app.web.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import xyz.ConstruTec.app.dto.ObraDetalhesDTO;
import xyz.ConstruTec.app.dto.LancamentoProdutoDTO;
import xyz.ConstruTec.app.model.Estoque;
import xyz.ConstruTec.app.model.MovimentacaoEstoque;
import xyz.ConstruTec.app.model.Obra;
import xyz.ConstruTec.app.model.Produto;
import xyz.ConstruTec.app.model.RetiradaProduto;
import xyz.ConstruTec.app.model.StatusObra;
import xyz.ConstruTec.app.model.Fornecedor;
import xyz.ConstruTec.app.model.Cliente;
import xyz.ConstruTec.app.service.ClienteService;
import xyz.ConstruTec.app.service.EstoqueService;
import xyz.ConstruTec.app.service.ObraService;
import xyz.ConstruTec.app.service.ProdutoService;
import xyz.ConstruTec.app.service.FornecedorService;
import xyz.ConstruTec.app.service.GeocodingService;
import xyz.ConstruTec.app.web.dto.StatusUpdateDTO;

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

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private GeocodingService geocodingService;

    @ModelAttribute("produtos")
    public List<Produto> getProdutos() {
        return produtoService.buscarTodos();
    }

    @GetMapping
    public String listarObras(Model model) {
        List<Obra> obras = obraService.buscarTodasObras();
        
        // Busca todos os produtos e seus estoques na matriz
        List<Produto> produtos = produtoService.buscarTodos();
        for (Produto produto : produtos) {
            // Busca o estoque na matriz para cada produto
            Estoque estoqueMatriz = estoqueService.buscarEstoqueProdutoMatriz(produto);
            produto.setEstoque(estoqueMatriz); // Associa o estoque ao produto
        }
        
        model.addAttribute("clientes", clienteService.buscarTodos());
        model.addAttribute("fornecedores", fornecedorService.buscarTodos());
        model.addAttribute("obra", new Obra());
        model.addAttribute("obras", obras);
        model.addAttribute("produtos", produtos);
        
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
                .filter(e -> e.getQuantidade() < 10 && e.getQuantidade() > 0)
                .count();
                
        // Calcula quantidade de produtos sem estoque
        long produtosSemEstoque = estoque.stream()
                .filter(e -> e.getQuantidade() == 0)
                .count();
                
        // Calcula quantidade de produtos com estoque normal
        long produtosEstoqueNormal = estoque.stream()
                .filter(e -> e.getQuantidade() >= 10)
                .count();

        // Busca todos os produtos com seus estoques
        List<Produto> produtos = produtoService.buscarTodos();
        produtos.forEach(produto -> {
            Estoque estoqueMatriz = estoqueService.buscarEstoqueProdutoMatriz(produto);
            produto.setEstoque(estoqueMatriz);
            
            // Busca a última movimentação do produto
            MovimentacaoEstoque ultimaMovimentacao = estoqueService.buscarUltimaMovimentacao(produto);
            produto.setUltimaMovimentacao(ultimaMovimentacao);
        });

        // Cria um novo DTO para o formulário
        LancamentoProdutoDTO lancamentoProdutoDTO = new LancamentoProdutoDTO();
        lancamentoProdutoDTO.setObraId(id);
        lancamentoProdutoDTO.setDataRetirada(LocalDate.now());

        model.addAttribute("obra", obra);
        model.addAttribute("estoque", estoque);
        model.addAttribute("movimentacoes", movimentacoes);
        model.addAttribute("produtosBaixoEstoque", produtosBaixoEstoque);
        model.addAttribute("produtosSemEstoque", produtosSemEstoque);
        model.addAttribute("produtosEstoqueNormal", produtosEstoqueNormal);
        model.addAttribute("produtos", produtos);
        model.addAttribute("fornecedores", fornecedorService.buscarTodos());
        model.addAttribute("lancamentoProdutoDTO", lancamentoProdutoDTO);

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

    @PostMapping("/salvar")
    public String salvarObra(@ModelAttribute Obra obra, @RequestParam("clienteId") Long clienteId, RedirectAttributes attr) {
        try {
            // Garante que uma nova obra tenha um status
            if (obra.getStatus() == null) {
                obra.setStatus(StatusObra.NAO_INICIADA);
            }
            
            // Busca o cliente pelo ID
            Cliente cliente = clienteService.buscarPorId(clienteId);
            if (cliente == null) {
                attr.addFlashAttribute("erro", "Cliente não encontrado");
                return "redirect:/obras";
            }
            obra.setCliente(cliente);
            
            // Valida o valor estimado
            if (obra.getValorEstimado() == null || obra.getValorEstimado().compareTo(BigDecimal.ZERO) < 0) {
                attr.addFlashAttribute("erro", "Valor estimado inválido");
                return "redirect:/obras";
            }
            
            // Geocodifica o endereço para obter latitude e longitude
            if (obra.getEndereco() != null && !obra.getEndereco().trim().isEmpty() &&
                obra.getCidade() != null && !obra.getCidade().trim().isEmpty() &&
                obra.getEstado() != null && !obra.getEstado().trim().isEmpty()) {
                
                System.out.println("Geocodificando endereço:");
                System.out.println("Endereço: " + obra.getEndereco());
                System.out.println("Cidade: " + obra.getCidade());
                System.out.println("Estado: " + obra.getEstado());
                
                // Primeiro, tenta com o endereço completo
                Map<String, Double> coordinates = geocodingService.geocodeAddress(
                    obra.getEndereco(),
                    "", // número será extraído do endereço
                    "", // bairro será extraído do endereço
                    obra.getCidade(),
                    obra.getEstado()
                );
                
                if (coordinates != null) {
                    System.out.println("Coordenadas encontradas (primeira tentativa):");
                    System.out.println("Latitude: " + coordinates.get("latitude"));
                    System.out.println("Longitude: " + coordinates.get("longitude"));
                    
                    obra.setLatitude(coordinates.get("latitude"));
                    obra.setLongitude(coordinates.get("longitude"));
                } else {
                    // Se não conseguir, tenta separar o endereço em partes
                    String[] partes = obra.getEndereco().split(",");
                    String logradouro = partes[0].trim();
                    String numero = partes.length > 1 ? partes[1].trim() : "";
                    String bairro = partes.length > 2 ? partes[2].trim() : "";
                    
                    System.out.println("Tentando com endereço separado:");
                    System.out.println("Logradouro: " + logradouro);
                    System.out.println("Número: " + numero);
                    System.out.println("Bairro: " + bairro);
                    
                    coordinates = geocodingService.geocodeAddress(
                        logradouro,
                        numero,
                        bairro,
                        obra.getCidade(),
                        obra.getEstado()
                    );
                    
                    if (coordinates != null) {
                        System.out.println("Coordenadas encontradas (segunda tentativa):");
                        System.out.println("Latitude: " + coordinates.get("latitude"));
                        System.out.println("Longitude: " + coordinates.get("longitude"));
                        
                        obra.setLatitude(coordinates.get("latitude"));
                        obra.setLongitude(coordinates.get("longitude"));
                    } else {
                        // Se ainda não conseguir, tenta com o endereço mais específico possível
                        String enderecoCompleto = String.format("%s, %s, %s - %s",
                            logradouro,
                            obra.getCidade(),
                            obra.getEstado(),
                            "Brasil"
                        );
                        
                        System.out.println("Tentando com endereço mais específico:");
                        System.out.println("Endereço: " + enderecoCompleto);
                        
                        coordinates = geocodingService.geocodeAddress(
                            enderecoCompleto,
                            "",
                            "",
                            obra.getCidade(),
                            obra.getEstado()
                        );
                        
                        if (coordinates != null) {
                            System.out.println("Coordenadas encontradas (terceira tentativa):");
                            System.out.println("Latitude: " + coordinates.get("latitude"));
                            System.out.println("Longitude: " + coordinates.get("longitude"));
                            
                            obra.setLatitude(coordinates.get("latitude"));
                            obra.setLongitude(coordinates.get("longitude"));
                        } else {
                            System.out.println("Não foi possível encontrar coordenadas para o endereço");
                            attr.addFlashAttribute("aviso", "Não foi possível obter as coordenadas do endereço. A obra será salva sem localização no mapa.");
                        }
                    }
                }
            }
            
            obraService.salvar(obra);
            attr.addFlashAttribute("mensagem", "Obra cadastrada com sucesso!");
            return "redirect:/obras";
        } catch (Exception e) {
            System.err.println("Erro ao salvar obra: " + e.getMessage());
            e.printStackTrace();
            attr.addFlashAttribute("erro", "Erro ao salvar obra: " + e.getMessage());
            return "redirect:/obras";
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> excluirObra(@PathVariable Long id) {
        boolean excluido = obraService.excluirObra(id);
        return excluido ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/lancar-produto")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String lancarProduto(@ModelAttribute @Valid LancamentoProdutoDTO lancamento,
                               BindingResult result,
                               RedirectAttributes attr) {
        try {
            System.out.println("\n=== Iniciando processo de lançamento de produto ===");
            System.out.println("Dados do lançamento:");
            System.out.println("Obra ID: " + lancamento.getObraId());
            System.out.println("Produto ID: " + lancamento.getProdutoId());
            System.out.println("Quantidade: " + lancamento.getQuantidade());
            System.out.println("Fornecedor ID: " + lancamento.getFornecedorId());
            System.out.println("Preço de Custo: " + lancamento.getPrecoCusto());
            System.out.println("Data de Retirada: " + lancamento.getDataRetirada());

            if (result.hasErrors()) {
                StringBuilder errors = new StringBuilder();
                result.getAllErrors().forEach(error -> {
                    System.err.println("Erro de validação: " + error.getDefaultMessage());
                    errors.append(error.getDefaultMessage()).append("; ");
                });
                attr.addFlashAttribute("error", errors.toString());
                return "redirect:/obras/" + lancamento.getObraId() + "/detalhes";
            }

            if (lancamento.getObraId() == null) {
                System.err.println("ID da obra não informado");
                attr.addFlashAttribute("error", "ID da obra não informado");
                return "redirect:/obras";
            }

            if (lancamento.getProdutoId() == null) {
                System.err.println("Produto não selecionado");
                attr.addFlashAttribute("error", "Produto não selecionado");
                return "redirect:/obras/" + lancamento.getObraId() + "/detalhes";
            }

            if (lancamento.getQuantidade() == null || lancamento.getQuantidade() <= 0) {
                System.err.println("Quantidade inválida: " + lancamento.getQuantidade());
                attr.addFlashAttribute("error", "Quantidade inválida");
                return "redirect:/obras/" + lancamento.getObraId() + "/detalhes";
            }

            if (lancamento.getFornecedorId() == null) {
                System.err.println("Fornecedor não selecionado");
                attr.addFlashAttribute("error", "Fornecedor não selecionado");
                return "redirect:/obras/" + lancamento.getObraId() + "/detalhes";
            }

            if (lancamento.getPrecoCusto() == null || lancamento.getPrecoCusto().compareTo(BigDecimal.ZERO) < 0) {
                System.err.println("Preço de custo inválido: " + lancamento.getPrecoCusto());
                attr.addFlashAttribute("error", "Preço de custo inválido");
                return "redirect:/obras/" + lancamento.getObraId() + "/detalhes";
            }

            if (lancamento.getDataRetirada() == null) {
                System.err.println("Data de retirada não informada");
                attr.addFlashAttribute("error", "Data de retirada não informada");
                return "redirect:/obras/" + lancamento.getObraId() + "/detalhes";
            }

            Obra obra = obraService.buscarPorId(lancamento.getObraId());
            if (obra == null) {
                System.err.println("Obra não encontrada com ID: " + lancamento.getObraId());
                attr.addFlashAttribute("error", "Obra não encontrada");
                return "redirect:/obras";
            }
            System.out.println("Obra encontrada: " + obra.getNome());

            Produto produto = produtoService.buscarPorId(lancamento.getProdutoId());
            if (produto == null) {
                System.err.println("Produto não encontrado com ID: " + lancamento.getProdutoId());
                attr.addFlashAttribute("error", "Produto não encontrado");
                return "redirect:/obras/" + lancamento.getObraId() + "/detalhes";
            }
            System.out.println("Produto encontrado: " + produto.getDescricao());

            Fornecedor fornecedor = fornecedorService.buscarPorId(lancamento.getFornecedorId());
            if (fornecedor == null) {
                System.err.println("Fornecedor não encontrado com ID: " + lancamento.getFornecedorId());
                attr.addFlashAttribute("error", "Fornecedor não encontrado");
                return "redirect:/obras/" + lancamento.getObraId() + "/detalhes";
            }
            System.out.println("Fornecedor encontrado: " + fornecedor.getNomeFantasia());

            // Verifica se há estoque suficiente
            Estoque estoqueMatriz = estoqueService.buscarEstoqueProdutoMatriz(produto);
            if (estoqueMatriz == null || estoqueMatriz.getQuantidade() < lancamento.getQuantidade()) {
                System.err.println("Estoque insuficiente. Disponível: " + 
                    (estoqueMatriz != null ? estoqueMatriz.getQuantidade() : 0) + 
                    ", Solicitado: " + lancamento.getQuantidade());
                attr.addFlashAttribute("error", "Quantidade insuficiente em estoque na matriz");
                return "redirect:/obras/" + lancamento.getObraId() + "/detalhes";
            }
            System.out.println("Estoque suficiente verificado. Disponível: " + estoqueMatriz.getQuantidade());

            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
            movimentacao.setProduto(produto);
            movimentacao.setQuantidade(lancamento.getQuantidade());
            movimentacao.setDestino(obra);
            movimentacao.setFornecedor(fornecedor);
            movimentacao.setPrecoCusto(lancamento.getPrecoCusto());
            movimentacao.setDataMovimentacao(LocalDateTime.now());

            System.out.println("Iniciando transferência no EstoqueService...");
            // Realiza a transferência dentro de uma transação
            estoqueService.realizarTransferencia(movimentacao);
            System.out.println("Transferência concluída com sucesso!");

            attr.addFlashAttribute("success", "Produto lançado com sucesso!");
            return "redirect:/obras/" + lancamento.getObraId() + "/detalhes";

        } catch (Exception e) {
            System.err.println("Erro ao lançar produto: " + e.getMessage());
            e.printStackTrace();
            attr.addFlashAttribute("error", "Erro ao lançar produto: " + e.getMessage());
            return "redirect:/obras/" + lancamento.getObraId() + "/detalhes";
        }
    }

    @GetMapping("/mapa/obras")
    @ResponseBody
    public List<Map<String, Object>> obrasParaMapa() {
        List<Obra> obras = obraService.buscarTodasObras();
        System.out.println("Total de obras encontradas: " + obras.size());
        
        List<Map<String, Object>> obrasParaMapa = obras.stream()
            .peek(obra -> {
                System.out.println("Obra: " + obra.getNome());
                System.out.println("Endereço: " + obra.getEndereco());
                System.out.println("Cidade: " + obra.getCidade());
                System.out.println("Estado: " + obra.getEstado());
                System.out.println("Latitude: " + obra.getLatitude());
                System.out.println("Longitude: " + obra.getLongitude());
                System.out.println("--------------------");
            })
            .filter(obra -> obra.getLatitude() != null && obra.getLongitude() != null)
            .map(obra -> {
                Map<String, Object> dadosObra = new HashMap<>();
                dadosObra.put("id", obra.getId());
                dadosObra.put("titulo", obra.getNome());
                dadosObra.put("descricao", String.format(
                    "Cliente: %s<br>Endereço: %s<br>Cidade: %s - %s<br>Valor Estimado: R$ %.2f",
                    obra.getCliente().getNome(),
                    obra.getEndereco(),
                    obra.getCidade(),
                    obra.getEstado(),
                    obra.getValorEstimado()
                ));
                dadosObra.put("latitude", obra.getLatitude());
                dadosObra.put("longitude", obra.getLongitude());
                dadosObra.put("status", obra.getStatus().getDescricao());
                
                // Define a cor com base no status
                String cor;
                switch (obra.getStatus()) {
                    case NAO_INICIADA:
                        cor = "#ffc107"; // Amarelo
                        break;
                    case EM_ANDAMENTO:
                        cor = "#007bff"; // Azul
                        break;
                    case CONCLUIDA:
                        cor = "#28a745"; // Verde
                        break;
                    case CANCELADA:
                        cor = "#dc3545"; // Vermelho
                        break;
                    default:
                        cor = "#6c757d"; // Cinza
                }
                dadosObra.put("cor", cor);
                
                return dadosObra;
            })
            .collect(Collectors.toList());
            
        System.out.println("Obras com coordenadas válidas: " + obrasParaMapa.size());
        return obrasParaMapa;
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<String> atualizarStatus(@PathVariable Long id, @RequestBody StatusUpdateDTO statusUpdate) {
        try {
            Obra obra = obraService.buscarPorId(id);
            if (obra == null) {
                return ResponseEntity.badRequest().body("Obra não encontrada");
            }

            StatusObra novoStatus = StatusObra.valueOf(statusUpdate.getStatus());
            StatusObra statusAnterior = obra.getStatus();
            
            // Verifica se é uma transição válida
            if (statusAnterior == StatusObra.EM_ANDAMENTO && novoStatus == StatusObra.CONCLUIDA) {
                // Atualiza a data de término
                obra.setDataTermino(LocalDate.now());
                // Remove as coordenadas do mapa
                obra.setLatitude(null);
                obra.setLongitude(null);
            } else if (novoStatus == StatusObra.CANCELADA) {
                // Se a obra for cancelada, também remove do mapa e atualiza a data de término
                obra.setDataTermino(LocalDate.now());
                obra.setLatitude(null);
                obra.setLongitude(null);
            }
            
            obra.setStatus(novoStatus);
            obra.setObservacoes(statusUpdate.getObservacoes());
            
            obraService.salvar(obra);
            return ResponseEntity.ok("Status atualizado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar status: " + e.getMessage());
        }
    }
}
