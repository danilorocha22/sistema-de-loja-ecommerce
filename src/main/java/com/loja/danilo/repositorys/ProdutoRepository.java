package com.loja.danilo.repositorys;

import com.loja.danilo.models.Produto;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author danilo
 */
@SuppressWarnings("all")
@Transactional
@Repository
public class ProdutoRepository {

    @PersistenceContext
    private EntityManager em;

    //Salar um Produto
    public void save(Produto produto) {
        em.persist(produto);
    }

    //Buscar um único Produto
    public Produto produto(Long id) {
        return em.find(Produto.class, id);
    }

    //Listar todos os Produtos
    public List<Produto> produtos() {
        Query query = em.createQuery("from Produto");
        return query.getResultList();
    }

    //Remove um único Produto
    public void remove(Long id) {
        Produto p = em.find(Produto.class, id);
        em.remove(p);
    }

    //Atualizar um único Produto
    public void update(Produto produto) {
        em.merge(produto);
    }

    //Buscar produtos especificados pelo cliente
    public List<Produto> buscarProduto(String buscarProduto) {
        String hql = "from Produto as p where p.nome like :buscarProduto";
        Query query = em.createQuery(hql, Produto.class);
        query.setParameter("buscarProduto", "%"+buscarProduto+"%");
        return query.getResultList();
    }
}
