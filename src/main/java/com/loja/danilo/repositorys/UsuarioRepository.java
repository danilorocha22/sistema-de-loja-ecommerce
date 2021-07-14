package com.loja.danilo.repositorys;

import com.loja.danilo.models.ClientePF;
import com.loja.danilo.models.Usuario;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * @author danilo
 */
@SuppressWarnings("all")
@Repository
public class UsuarioRepository {

    @PersistenceContext
    private EntityManager em;

    //Retorna usuario pelo login
    public Usuario usuario(String login) {
        String hql = "from Usuario as u where u.login = :login";
        TypedQuery<Usuario> query = em.createQuery(hql, Usuario.class);
        query.setParameter("login",login);
        return query.getSingleResult();
    }

    //Retorna usuario pelo cliente
    public Usuario usuarioPeloCliente(ClientePF cliente) {
        String hql = "from Usuario as u where u.cliente = :cliente";
        TypedQuery<Usuario> query = em.createQuery(hql, Usuario.class);
        query.setParameter("cliente",cliente);
        return query.getSingleResult();
    }

    public void save(Usuario usuario) {
        em.persist(usuario);
    }

}
