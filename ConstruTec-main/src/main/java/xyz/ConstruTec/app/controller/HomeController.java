package xyz.ConstruTec.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import xyz.ConstruTec.app.model.Home;
import xyz.ConstruTec.app.service.HomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class HomeController {
    
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    
    @Autowired
    private HomeService homeService;

    @GetMapping("/")
    public String home(ModelMap model) {
        logger.info("Iniciando carregamento da página home");
        try {
            logger.debug("Chamando HomeService.getResumo()");
            Home home = homeService.getResumo();
            
            if (home == null) {
                logger.error("HomeService.getResumo() retornou null");
                throw new RuntimeException("Erro ao carregar dados do dashboard");
            }
            
            logger.debug("Dados obtidos do HomeService: qtd_cliente={}, qtd_produto={}, qtd_fornecedor={}, qtd_obra={}", 
                home.getQtd_cliente(), home.getQtd_produto(), home.getQtd_fornecedor(), home.getQtd_obra());
            
            model.addAttribute("home", home);
            logger.info("Página home carregada com sucesso");
            
            return "home";
        } catch (Exception e) {
            logger.error("Erro ao carregar a página home: {}", e.getMessage(), e);
            model.addAttribute("erro", "Ocorreu um erro ao carregar os dados do dashboard. Por favor, tente novamente mais tarde.");
            return "error/500";
        }
    }
} 