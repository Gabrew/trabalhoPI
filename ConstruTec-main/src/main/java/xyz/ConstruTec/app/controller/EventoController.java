package xyz.ConstruTec.app.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xyz.ConstruTec.app.model.Evento;
import xyz.ConstruTec.app.model.Evento.TipoEvento;
import xyz.ConstruTec.app.service.EventoService;

@Controller
@RequestMapping("/eventos")
public class EventoController {
    
    @Autowired
    private EventoService eventoService;
    
    @GetMapping
    public ResponseEntity<List<Evento>> getEventosEntreDatas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(eventoService.buscarEventosEntreDatas(inicio, fim));
    }
    
    @GetMapping("/proximos/{quantidade}")
    public ResponseEntity<List<Evento>> getProximosEventos(@PathVariable int quantidade) {
        return ResponseEntity.ok(eventoService.buscarProximosEventos(quantidade));
    }
    
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Evento>> getEventosPorTipo(@PathVariable TipoEvento tipo) {
        return ResponseEntity.ok(eventoService.buscarEventosPorTipo(tipo));
    }
    
    @PostMapping
    public ResponseEntity<Evento> criarEvento(@RequestBody Evento evento) {
        Evento novoEvento = eventoService.criarEvento(
            evento.getTitulo(),
            evento.getDescricao(),
            evento.getTipo(),
            evento.getInicio(),
            evento.getFim()
        );
        return ResponseEntity.ok(novoEvento);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarEvento(@PathVariable Long id, @RequestBody Evento evento) {
        eventoService.atualizarEvento(
            id,
            evento.getTitulo(),
            evento.getDescricao(),
            evento.getTipo(),
            evento.getInicio(),
            evento.getFim()
        );
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirEvento(@PathVariable Long id) {
        eventoService.excluirEvento(id);
        return ResponseEntity.ok().build();
    }
} 