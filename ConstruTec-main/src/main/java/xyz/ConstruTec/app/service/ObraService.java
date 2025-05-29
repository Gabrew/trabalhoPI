package xyz.ConstruTec.app.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.ConstruTec.app.dao.ObraDao;
import xyz.ConstruTec.app.dao.RetiradaProdutoDao;
import xyz.ConstruTec.app.dao.RecebimentoDao;
import xyz.ConstruTec.app.dto.ObraDetalhesDTO;
import xyz.ConstruTec.app.model.Obra;
import xyz.ConstruTec.app.model.RetiradaProduto;

@Service
public class ObraService {

    @Autowired
    private ObraDao obraDao;

    @Autowired
    private RetiradaProdutoDao retiradaProdutoDao;

    @Autowired
    private RecebimentoDao recebimentoDao;

    // Buscar todas as obras para exibir na tela
    public List<Obra> buscarTodasObras() {
        return obraDao.findAll();
    }

    // Buscar detalhes da obra com DTO para modal
    public ObraDetalhesDTO buscarDetalhesObra(Long id) {
        Optional<Obra> optional = obraDao.findById(id);
        if (!optional.isPresent()) {
            return null;
        }
        Obra obra = optional.get();

        ObraDetalhesDTO dto = new ObraDetalhesDTO();
        dto.setId(obra.getId());
        dto.setDataInicio(obra.getDataInicio());
        dto.setDataTermino(obra.getDataTermino());

        if (obra.getCliente() != null) {
            ObraDetalhesDTO.ClienteDTO clienteDTO = new ObraDetalhesDTO.ClienteDTO();
            clienteDTO.setId(obra.getCliente().getId());
            clienteDTO.setNomeCompleto(obra.getCliente().getNome() + " " + obra.getCliente().getSobrenome());

            if (obra.getCliente().getEndereco() != null) {
                ObraDetalhesDTO.EnderecoDTO enderecoDTO = new ObraDetalhesDTO.EnderecoDTO();
                enderecoDTO.setLogradouro(obra.getCliente().getEndereco().getLogradouro());
                enderecoDTO.setNumero(obra.getCliente().getEndereco().getNumero());
                enderecoDTO.setBairro(obra.getCliente().getEndereco().getBairro());
                enderecoDTO.setCidade(obra.getCliente().getEndereco().getCidade());
                enderecoDTO.setUf(obra.getCliente().getEndereco().getUf() != null ? obra.getCliente().getEndereco().getUf().name() : null);
                clienteDTO.setEndereco(enderecoDTO);
            }

            dto.setCliente(clienteDTO);
        }

        if (obra.getRetiradaProdutos() != null) {
            List<ObraDetalhesDTO.RetiradaDTO> retiradasDTO = obra.getRetiradaProdutos().stream().map(rp -> {
                ObraDetalhesDTO.RetiradaDTO rDTO = new ObraDetalhesDTO.RetiradaDTO();
                rDTO.setId(rp.getId());
                rDTO.setDescricao(rp.getDescricao());
                rDTO.setQuantidade(rp.getQuantidade());
                return rDTO;
            }).collect(Collectors.toList());
            dto.setRetiradas(retiradasDTO);
        }

        if (obra.getRecebimentos() != null) {
            List<ObraDetalhesDTO.RecebimentoDTO> recebimentosDTO = obra.getRecebimentos().stream().map(r -> {
                ObraDetalhesDTO.RecebimentoDTO recDTO = new ObraDetalhesDTO.RecebimentoDTO();
                recDTO.setId(r.getId());
                recDTO.setValor(r.getValor());
                recDTO.setData(r.getData());
                return recDTO;
            }).collect(Collectors.toList());
            dto.setRecebimentos(recebimentosDTO);
        }

        dto.setCustoTotal(BigDecimal.ZERO);

        return dto;
    }

    // Excluir obra por id
    @Transactional
    public boolean excluirObra(Long id) {
        if (obraDao.existsById(id)) {
            obraDao.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public void salvar(Obra obra) {
        obraDao.save(obra);
    }


    // Adicionar retirada a obra
    @Transactional
    public boolean adicionarRetirada(Long obraId, RetiradaProduto retirada) {
        Optional<Obra> optional = obraDao.findById(obraId);
        if (!optional.isPresent()) {
            return false;
        }
        Obra obra = optional.get();

        retirada.setObra(obra);
        retiradaProdutoDao.save(retirada);

        // Também adiciona na lista da obra (se gerenciar bidirecional)
        obra.getRetiradaProdutos().add(retirada);
        obraDao.save(obra);

        return true;
    }
}
