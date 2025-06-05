package xyz.ConstruTec.app.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SenhaUtil {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senha = "admin123";
        String hash = encoder.encode(senha);
        System.out.println("Senha original: " + senha);
        System.out.println("Hash gerado: " + hash);
        
        // Verificar se o hash está correto
        boolean matches = encoder.matches(senha, hash);
        System.out.println("Hash válido: " + matches);
        
        // Verificar se o hash atual do banco está correto
        String hashAtual = "$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a";
        boolean matchesAtual = encoder.matches(senha, hashAtual);
        System.out.println("Hash atual válido: " + matchesAtual);
    }
} 