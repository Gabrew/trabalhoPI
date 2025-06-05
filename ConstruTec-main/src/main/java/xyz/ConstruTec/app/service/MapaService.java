package xyz.ConstruTec.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.ConstruTec.app.dao.ObraDao;
import xyz.ConstruTec.app.model.MapaObra;
import xyz.ConstruTec.app.model.Obra;

@Service
public class MapaService {
    
    @Autowired
    private ObraDao obraDao;
    
    public List<MapaObra> getObrasParaMapa() {
        List<MapaObra> obrasParaMapa = new ArrayList<>();
        List<Obra> obras = obraDao.findAll();
        
        for (Obra obra : obras) {
            String cor;
            switch (obra.getStatus()) {
                case "Em Andamento":
                    cor = "#28a745"; // Verde
                    break;
                case "Atrasada":
                    cor = "#dc3545"; // Vermelho
                    break;
                case "Planejamento":
                    cor = "#ffc107"; // Amarelo
                    break;
                case "Concluída":
                    cor = "#007bff"; // Azul
                    break;
                default:
                    cor = "#6c757d"; // Cinza
            }
            
            obrasParaMapa.add(new MapaObra(
                obra.getId(),
                obra.getNome(),
                obra.getDescricao(),
                obra.getStatus(),
                obra.getLatitude(),
                obra.getLongitude(),
                cor
            ));
        }
        
        return obrasParaMapa;
    }
} 