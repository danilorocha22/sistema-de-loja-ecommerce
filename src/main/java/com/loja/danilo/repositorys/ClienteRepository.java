package com.loja.danilo.repositorys;

import com.loja.danilo.models.ClientePF;
import com.loja.danilo.models.Usuario;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author danilo
 */
@SuppressWarnings("all")
@Transactional
@Repository
public class ClienteRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(ClientePF clientePF){
        em.persist(clientePF);
    }

    public ClientePF clientePF(Long id) {
        return em.find(ClientePF.class, id);
    }

    //Retorna um cliente pelo usuário
    public ClientePF clientePF(Usuario usuario) {
        String hql = "from ClientePF as c where c.usuario = :usuario";
        TypedQuery<ClientePF> query = em.createQuery(hql, ClientePF.class);
        query.setParameter("usuario",usuario);
        return query.getSingleResult();
    }

    //Retorna uma lista de clientes
    public List<ClientePF> clientesPF(){
        Query query = em.createQuery("from ClientePF");
        return query.getResultList();
    }

    //Remove um cliente
    public void remove(Long id) {
        ClientePF clientePF = em.find(ClientePF.class, id);
        em.remove(clientePF);
    }

    //Atualiza um cliente
    public void update(ClientePF clientePF) {
        em.merge(clientePF);
    }

    //Buscar uma lista de Clientes pelo nome
    public List<ClientePF> filtrarNome(String filtrarNome) {
        String hql = "from ClientePF as c where c.nome like :filtrarNome";
        Query query = em.createQuery(hql, ClientePF.class);
        query.setParameter("filtrarNome", "%"+filtrarNome+"%");
        return query.getResultList();
    }

    //Busca um Cliente pelo nome
    public ClientePF buscaClientePeloNome(String nome) {
        String hql = "from ClientePF as c where c.nome like :nome";
        TypedQuery<ClientePF> query = em.createQuery(hql, ClientePF.class);
        query.setParameter("nome", nome);
        return query.getSingleResult();
    }

}
