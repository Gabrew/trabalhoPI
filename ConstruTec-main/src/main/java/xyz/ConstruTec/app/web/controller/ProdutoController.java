package xyz.ConstruTec.app.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import xyz.ConstruTec.app.model.Categoria;
import xyz.ConstruTec.app.model.Fornecedor;
import xyz.ConstruTec.app.model.Produto;
import xyz.ConstruTec.app.service.CategoriaService;
import xyz.ConstruTec.app.service.FornecedorService;
import xyz.ConstruTec.app.service.ProdutoService;
import xyz.ConstruTec.app.service.EstoqueService;
import xyz.ConstruTec.app.util.PaginacaoUtil;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {
	
	//Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "D://images//";
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private FornecedorService fornecedorService;
    
    @Autowired
    private EstoqueService estoqueService;

    @GetMapping
    public String index() {
        return "redirect:/produtos/listar";
    }

	@GetMapping("/cadastrar")
	public String cadastrar(Produto produto, ModelMap model) {
		List<Categoria> categorias = categoriaService.buscarTodos();
		List<Fornecedor> fornecedores = fornecedorService.buscarTodos();
		model.addAttribute("categorias", categorias);
		model.addAttribute("fornecedores", fornecedores);
		return "produto/cadastro";
	}
	
	@GetMapping("/listar")
	public String listar(ModelMap model, @RequestParam("page") Optional<Integer> page) {
		int paginaAtual = page.orElse(1);
		PaginacaoUtil<Produto> pageProduto = produtoService.buscarPorPagina(paginaAtual);
		List<Categoria> categorias = categoriaService.buscarTodos();
		List<Fornecedor> fornecedores = fornecedorService.buscarTodos();
		
		// Log para debug
		System.out.println("Total de registros: " + pageProduto.getTotalDePagina() * pageProduto.getTamanho());
		System.out.println("Registros na página atual: " + pageProduto.getRegistros().size());
		System.out.println("Página atual: " + paginaAtual);
		System.out.println("Total de páginas: " + pageProduto.getTotalDePagina());
		
		// Para cada produto, busca a última movimentação
		pageProduto.getRegistros().forEach(produto -> {
			produto.setUltimaMovimentacao(estoqueService.buscarUltimaMovimentacao(produto));
		});
		
		model.addAttribute("pageProduto", pageProduto);
		model.addAttribute("categorias", categorias);
		model.addAttribute("fornecedores", fornecedores);
		return "produto/lista";
	}
	
	@PostMapping("/salvar")
	public String salvar(@Valid Produto produto, BindingResult result, RedirectAttributes attr, ModelMap model) {
		//Verifica erros de validação do formulário
		if (result.hasErrors()) {
			return cadastrar(produto, model);
		}
		
		produtoService.salvar(produto);
		attr.addFlashAttribute("success", "Produto cadastrado com sucesso.");
		return "redirect:/produtos/cadastrar";
	}
	
	@GetMapping("/inativar/{id}")
	public String inativar(@PathVariable("id") Long id, RedirectAttributes attr) {
		produtoService.inativar(id);
		attr.addFlashAttribute("success", "Produto inativado com sucesso.");
		return "redirect:/produtos/listar";
	}
	
	@GetMapping("/ativar/{id}")
	public String ativar(@PathVariable("id") Long id, RedirectAttributes attr) {
		produtoService.ativar(id);
		attr.addFlashAttribute("success", "Produto ativado com sucesso.");
		return "redirect:/produtos/listar";
	}
	
	@GetMapping("/exibeImagem/{imagem}")
	@ResponseBody
	public byte[] getImagem(@PathVariable("imagem") String imagem) throws IOException {
		File imagemArquivo = new File(UPLOADED_FOLDER + imagem);
		return Files.readAllBytes(imagemArquivo.toPath());
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		produtoService.excluir(id);
		attr.addFlashAttribute("success", "Produto excluído com sucesso.");
		return "redirect:/produtos/listar";
	}
	
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		Produto produto = produtoService.buscarPorId(id);
		List<Categoria> categorias = categoriaService.buscarTodos();
		List<Fornecedor> fornecedores = fornecedorService.buscarTodos();
		model.addAttribute("produto", produto);
		model.addAttribute("categorias", categorias);
		model.addAttribute("fornecedores", fornecedores);
		return "produto/cadastro";
	}
	
	@PostMapping("/editar")
	public String editar(@Valid Produto produto, BindingResult result, RedirectAttributes attr, ModelMap model) {
		
		//Verifica erros de validação do formulário
		if (result.hasErrors()) {
			return cadastrar(produto, model);
		}
		
		produtoService.editar(produto);
		attr.addFlashAttribute("success", "Produto alterado com sucesso.");
		return "redirect:/produtos/cadastrar";
	}
	
	@GetMapping("/buscar/codigo")
	public String getPorCodigo(@RequestParam("codigo") Long codigo, ModelMap model, @RequestParam("page") Optional<Integer> page) {
		int paginaAtual = page.orElse(1);
		PaginacaoUtil<Produto> pageProduto = produtoService.buscarPorPaginaCodigo(codigo, paginaAtual);
		List<Categoria> categorias = categoriaService.buscarTodos();
		List<Fornecedor> fornecedores = fornecedorService.buscarTodos();
		model.addAttribute("pageProduto", pageProduto);
		model.addAttribute("categorias", categorias);
		model.addAttribute("fornecedores", fornecedores);
		return "produto/lista";
	}
	
	@GetMapping("/buscar/nome")
	public String getPorNome(@RequestParam("nome") String nome, ModelMap model, @RequestParam("page") Optional<Integer> page) {
		int paginaAtual = page.orElse(1);
		PaginacaoUtil<Produto> pageProduto = produtoService.buscarPorPaginaNome(nome, paginaAtual);
		List<Categoria> categorias = categoriaService.buscarTodos();
		List<Fornecedor> fornecedores = fornecedorService.buscarTodos();
		model.addAttribute("pageProduto", pageProduto);
		model.addAttribute("categorias", categorias);
		model.addAttribute("fornecedores", fornecedores);
		return "produto/lista";
	}
	
	@GetMapping("/buscar/categoria")
	public String getPorCategoria(@RequestParam("categoria") Categoria categoria, ModelMap model, @RequestParam("page") Optional<Integer> page) {
		int paginaAtual = page.orElse(1);
		PaginacaoUtil<Produto> pageProduto = produtoService.buscarPorPaginaCategoria(categoria, paginaAtual);
		List<Categoria> categorias = categoriaService.buscarTodos();
		List<Fornecedor> fornecedores = fornecedorService.buscarTodos();
		model.addAttribute("pageProduto", pageProduto);
		model.addAttribute("categorias", categorias);
		model.addAttribute("fornecedores", fornecedores);
		return "produto/lista";
	}
	
	@GetMapping("/buscar/fornecedor")
	public String getPorFornecedor(@RequestParam("idFornecedor") Long idFornecedor, ModelMap model, @RequestParam("page") Optional<Integer> page) {
		int paginaAtual = page.orElse(1);
		Fornecedor fornecedor = fornecedorService.buscarPorId(idFornecedor);
		PaginacaoUtil<Produto> pageProduto = produtoService.buscarPorPaginaFornecedor(fornecedor, paginaAtual);
		List<Categoria> categorias = categoriaService.buscarTodos();
		List<Fornecedor> fornecedores = fornecedorService.buscarTodos();
		model.addAttribute("pageProduto", pageProduto);
		model.addAttribute("categorias", categorias);
		model.addAttribute("fornecedores", fornecedores);
		return "produto/lista";
	}
	
	@GetMapping("/buscar/referencia")
	public String getPorReferencia(@RequestParam("referencia") String referencia, ModelMap model, @RequestParam("page") Optional<Integer> page) {
		int paginaAtual = page.orElse(1);
		PaginacaoUtil<Produto> pageProduto = produtoService.buscarPorPaginaReferencia(referencia, paginaAtual);
		model.addAttribute("pageProduto", pageProduto);
		return "produto/lista";
	}
	
	@ModelAttribute("fornecedores")
	public List<Fornecedor> getFornecedores(){
		return fornecedorService.buscarTodos();
	}
	
	@ModelAttribute("categorias")
	public Categoria[] getCategorias() {
		return Categoria.values();
	}
	
	
	private String uploadImagem(MultipartFile arquivo) {
		//Upload da Foto do Produto (Não é obrigatório)
		try {
			byte[] bytes = arquivo.getBytes();
			String novoNome = Math.random() + arquivo.getOriginalFilename();
			String caminhoComNomeArquivo = UPLOADED_FOLDER + novoNome;
			Path path = Paths.get(caminhoComNomeArquivo);
			Files.write(path, bytes);
			return novoNome;
		}catch (Exception e) {
			throw new MultipartException(e.getMessage());
		}
			
	}
	
}
