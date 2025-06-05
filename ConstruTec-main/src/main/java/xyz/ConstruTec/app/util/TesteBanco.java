package xyz.ConstruTec.app.util;

import java.sql.Connection;
import java.sql.DriverManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TesteBanco {
    public static void main(String[] args) {
        try {
            // Carregar o driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Configurações de conexão
            String url = "jdbc:mysql://localhost:3308/controle_estoque?useSSL=false";
            String user = "root";
            String password = "";
            
            System.out.println("Tentando conectar ao banco de dados...");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conexão estabelecida com sucesso!");
            
            // Criar usuário de teste
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String senhaHash = encoder.encode("123456");
            
            // Testar queries
            var stmt = conn.createStatement();
            
            // Limpar dados existentes
            System.out.println("Limpando dados existentes...");
            stmt.executeUpdate("DELETE FROM usuarios_roles");
            stmt.executeUpdate("DELETE FROM roles");
            stmt.executeUpdate("DELETE FROM usuarios");
            
            // Criar role
            System.out.println("Criando role...");
            stmt.executeUpdate("INSERT INTO roles (nome_role) VALUES ('ROLE_ADMIN')");
            
            // Criar usuário
            System.out.println("Criando usuário...");
            stmt.executeUpdate("INSERT INTO usuarios (login, nome_completo, senha) VALUES ('admin', 'Administrador', '" + senhaHash + "')");
            
            // Relacionar usuário com role
            System.out.println("Relacionando usuário com role...");
            stmt.executeUpdate("INSERT INTO usuarios_roles (usuario_id, role_id) SELECT u.id, r.id FROM usuarios u, roles r WHERE u.login = 'admin' AND r.nome_role = 'ROLE_ADMIN'");
            
            // Verificar dados
            System.out.println("\nVerificando usuários:");
            var rs = stmt.executeQuery("SELECT * FROM usuarios");
            while(rs.next()) {
                System.out.println("ID: " + rs.getLong("id"));
                System.out.println("Login: " + rs.getString("login"));
                System.out.println("Senha hash: " + rs.getString("senha"));
            }
            
            System.out.println("\nVerificando roles:");
            rs = stmt.executeQuery("SELECT * FROM roles");
            while(rs.next()) {
                System.out.println("ID: " + rs.getLong("id"));
                System.out.println("Role: " + rs.getString("nome_role"));
            }
            
            System.out.println("\nVerificando relacionamentos:");
            rs = stmt.executeQuery("SELECT u.login, r.nome_role FROM usuarios u JOIN usuarios_roles ur ON u.id = ur.usuario_id JOIN roles r ON r.id = ur.role_id");
            while(rs.next()) {
                System.out.println("Usuário: " + rs.getString("login"));
                System.out.println("Role: " + rs.getString("nome_role"));
            }
            
            conn.close();
            System.out.println("\nTestes concluídos com sucesso!");
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 