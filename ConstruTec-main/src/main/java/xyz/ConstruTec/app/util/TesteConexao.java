package xyz.ConstruTec.app.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class TesteConexao {
    public static void main(String[] args) {
        try {
            String url = "jdbc:mysql://localhost:3308/controle_estoque";
            String user = "root";
            String password = "";
            
            System.out.println("Tentando conectar ao banco de dados...");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conexão estabelecida com sucesso!");
            
            // Testar uma query simples
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery("SELECT * FROM usuarios");
            while(rs.next()) {
                System.out.println("Usuário encontrado: " + rs.getString("login"));
                System.out.println("Senha hash: " + rs.getString("senha"));
            }
            
            conn.close();
            System.out.println("Conexão fechada com sucesso!");
            
        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 