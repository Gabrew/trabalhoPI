package xyz.ConstruTec.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import xyz.ConstruTec.app.model.ChartData;
import xyz.ConstruTec.app.service.ChartService;

@Controller
@RequestMapping("/charts")
public class ChartController {
    
    @Autowired
    private ChartService chartService;
    
    @GetMapping("/movimentacao-estoque")
    public ResponseEntity<ChartData> getMovimentacaoEstoque() {
        return ResponseEntity.ok(chartService.getMovimentacaoEstoque());
    }
    
    @GetMapping("/progresso-obras")
    public ResponseEntity<ChartData> getProgressoObras() {
        return ResponseEntity.ok(chartService.getProgressoObras());
    }
    
    @GetMapping("/distribuicao-produtos")
    public ResponseEntity<ChartData> getDistribuicaoProdutos() {
        return ResponseEntity.ok(chartService.getDistribuicaoProdutos());
    }
} 