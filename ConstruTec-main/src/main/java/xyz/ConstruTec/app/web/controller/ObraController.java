package xyz.ConstruTec.app.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.ConstruTec.app.service.ProdutoService;
import xyz.ConstruTec.app.dto.ObraDetalhesDTO;
import xyz.ConstruTec.app.model.Obra;
import xyz.ConstruTec.app.model.Produto;
import xyz.ConstruTec.app.model.RetiradaProduto;
import xyz.ConstruTec.app.service.ClienteService;
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

    @ModelAttribute("produtos")
    public List<Produto> getProdutos() {
    return produtoService.buscarTodos(); // ou equivalente
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
    @ResponseBody
    public ResponseEntity<ObraDetalhesDTO> detalhesObra(@PathVariable Long id) {
        ObraDetalhesDTO dto = obraService.buscarDetalhesObra(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/nova")
    public String novaObraForm(Model model) {
        model.addAttribute("obra", new Obra());
        model.addAttribute("clientes", clienteService.buscarTodos());
        return "obra/obras";
    }

    @PostMapping
    public String salvarObra(@ModelAttribute Obra obra) {
        obraService.salvar(obra);
        return "redirect:/obras";
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> excluirObra(@PathVariable Long id) {
        boolean excluido = obraService.excluirObra(id);
        return excluido ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/retiradas")
    @ResponseBody
    public ResponseEntity<Void> adicionarRetirada(@PathVariable Long id, @RequestBody RetiradaProduto retirada) {
        boolean adicionou = obraService.adicionarRetirada(id, retirada);
        return adicionou ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
