package xyz.ConstruTec.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.ConstruTec.app.dao.UsuarioDao;
import xyz.ConstruTec.app.model.Usuario;

@Service
public class UsuarioService implements UserDetailsService {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioDao usuarioDao;
    
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            logger.debug("Tentando autenticar usuário: {}", username);
            
            Usuario usuario = usuarioDao.findByLogin(username);
            if (usuario == null) {
                logger.error("Usuário não encontrado: {}", username);
                throw new UsernameNotFoundException("Usuário não encontrado");
            }
            
            logger.debug("Usuário encontrado: {}", usuario.getUsername());
            logger.debug("Roles do usuário: {}", usuario.getAuthorities());
            logger.debug("Senha hash do usuário: {}", usuario.getPassword());
            
            return User.builder()
                    .username(usuario.getUsername())
                    .password(usuario.getPassword())
                    .authorities(usuario.getAuthorities())
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();
                    
        } catch (Exception e) {
            logger.error("Erro ao autenticar usuário: {}", username, e);
            throw new UsernameNotFoundException("Erro ao autenticar usuário", e);
        }
    }
}
