package xyz.ConstruTec.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import xyz.ConstruTec.app.model.Alerta;
import xyz.ConstruTec.app.service.AlertaService;

@Controller
@RequestMapping("/alertas")
public class AlertaController {
    
    @Autowired
    private AlertaService alertaService;
    
    @GetMapping("/nao-lidos")
    public ResponseEntity<List<Alerta>> getAlertasNaoLidos() {
        return ResponseEntity.ok(alertaService.buscarAlertasNaoLidos());
    }
    
    @GetMapping("/ultimos/{quantidade}")
    public ResponseEntity<List<Alerta>> getUltimosAlertas(@PathVariable int quantidade) {
        return ResponseEntity.ok(alertaService.buscarUltimosAlertas(quantidade));
    }
    
    @PostMapping("/{id}/marcar-como-lido")
    public ResponseEntity<Void> marcarComoLido(@PathVariable Long id) {
        alertaService.marcarComoLido(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/marcar-todos-como-lidos")
    public ResponseEntity<Void> marcarTodosComoLidos() {
        alertaService.marcarTodosComoLidos();
        return ResponseEntity.ok().build();
    }
} 