package xyz.ConstruTec.app.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.ConstruTec.app.model.Categoria;

@Service
@Transactional(readOnly = true)
public class CategoriaService {

    public List<Categoria> buscarTodos() {
        return Arrays.asList(Categoria.values());
    }

    public Categoria buscarPorId(Long id) {
        return Arrays.stream(Categoria.values())
                .filter(c -> c.getCodigo() == id)
                .findFirst()
                .orElse(null);
    }
} 