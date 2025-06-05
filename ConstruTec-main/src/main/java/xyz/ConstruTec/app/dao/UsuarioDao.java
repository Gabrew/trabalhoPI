package xyz.ConstruTec.app.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ConstruTec.app.model.Usuario;

@Repository
public class UsuarioDao extends AbstractDao<Usuario, Long> {
	
	private static final Logger logger = LoggerFactory.getLogger(UsuarioDao.class);
	
	public Usuario findByLogin(String login) {
		logger.debug("Buscando usuário por login: {}", login);
		
		try {
			List<Usuario> usuarios = createQuery(
				"select distinct u from Usuario u " +
				"left join fetch u.roles " +
				"where u.login = ?1", 
				login
			);
			
			if (usuarios.isEmpty()) {
				logger.debug("Nenhum usuário encontrado com o login: {}", login);
				return null;
			}
			
			Usuario usuario = usuarios.get(0);
			logger.debug("Usuário encontrado: {}, authorities: {}", usuario.getLogin(), usuario.getAuthorities());
			return usuario;
			
		} catch (Exception e) {
			logger.error("Erro ao buscar usuário por login: {}", login, e);
			throw e;
		}
	}
	
}
