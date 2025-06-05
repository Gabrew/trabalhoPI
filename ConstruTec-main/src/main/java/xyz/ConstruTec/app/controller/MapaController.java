package xyz.ConstruTec.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import xyz.ConstruTec.app.model.MapaObra;
import xyz.ConstruTec.app.service.MapaService;

@Controller
@RequestMapping("/mapa")
public class MapaController {
    
    @Autowired
    private MapaService mapaService;
    
    @GetMapping("/obras")
    public ResponseEntity<List<MapaObra>> getObrasParaMapa() {
        return ResponseEntity.ok(mapaService.getObrasParaMapa());
    }
} 