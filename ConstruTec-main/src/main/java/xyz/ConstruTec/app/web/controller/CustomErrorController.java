package xyz.ConstruTec.app.web.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = "Ocorreu um erro inesperado.";
        String errorTitle = "Erro";
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                errorTitle = "Página não encontrada";
                errorMessage = "A página que você está procurando não existe.";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorTitle = "Erro interno do servidor";
                errorMessage = "Ocorreu um erro interno no servidor. Por favor, tente novamente mais tarde.";
            }
            else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                errorTitle = "Acesso negado";
                errorMessage = "Você não tem permissão para acessar esta página.";
            }
        }
        
        model.addAttribute("errorTitle", errorTitle);
        model.addAttribute("errorMessage", errorMessage);
        
        return "error/error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
} 