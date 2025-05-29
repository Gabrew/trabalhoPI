package xyz.ConstruTec.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.ConstruTec.app.dao.LancamentoProdutoObraDao;
import xyz.ConstruTec.app.model.LancamentoProdutoObra;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LancamentoProdutoObraService {

    @Autowired
    private LancamentoProdutoObraDao dao;

    public List<LancamentoProdutoObra> listarPorObra(Long obraId) {
        return dao.findByObra_Id(obraId);
    }

    @Transactional
    public void adicionarLancamento(LancamentoProdutoObra lancamento) {
        if (lancamento.getData() == null) {
            lancamento.setData(LocalDate.now());
        }
        dao.save(lancamento);
    }

    @Transactional
    public boolean excluirPorId(Long id) {
        Optional<LancamentoProdutoObra> lanc = dao.findById(id);
        if (lanc.isPresent()) {
            dao.deleteById(id);
            return true;
        }
        return false;
    }
}
