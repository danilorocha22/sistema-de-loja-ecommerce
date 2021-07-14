package com.loja.danilo.repositorys;

import com.loja.danilo.models.ItemVenda;
import com.loja.danilo.models.Produto;
import com.loja.danilo.models.Venda;
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
public class ItemVendaRepository {

    @PersistenceContext
    private EntityManager em;

    //Buscar uma lista de ItensVenda
    public List<ItemVenda> itensVenda(Long idVenda){
        String hql = "from ItemVenda as iv where iv.venda.id = :idVenda";
        Query query = em.createQuery(hql, ItemVenda.class);
        query.setParameter("idVenda", idVenda);
        return query.getResultList();
    }

    //Busca uma lista de produtos vendidos
    public List<ItemVenda> buscaProdutoVendido(Produto produto) {
        String hql = "from ItemVenda as iv where iv.produto = :produto";
        TypedQuery<ItemVenda> query = em.createQuery(hql, ItemVenda.class);
        query.setParameter("produto", produto);
        return query.getResultList();
    }

}
