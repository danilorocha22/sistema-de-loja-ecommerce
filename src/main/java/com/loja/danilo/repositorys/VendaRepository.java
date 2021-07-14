package com.loja.danilo.repositorys;

import com.loja.danilo.models.ClientePF;
import com.loja.danilo.models.Venda;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

/**
 * @author danilo
 */
@SuppressWarnings("all")
@Transactional
@Repository
public class VendaRepository {

    @PersistenceContext
    private EntityManager em;

    //Salvar uma Venda
    public void save(Venda venda){
        em.persist(venda);
    }

    //Buscar uma unica Venda
    public Venda venda(Long id){
        return em.find(Venda.class, id);
    }

    //Buscar lista de Vendas
    public List<Venda> vendas(){
        Query query = em.createQuery("from Venda");
        return query.getResultList();
    }

    //Buscar lista de Vendas pela data
    public List<Venda> vendasPelaData(LocalDate filtrarData) {
        String hql = "from Venda as v where v.localDate = :filtrarData";
        Query query = em.createQuery(hql, Venda.class);
        query.setParameter("filtrarData", filtrarData);
        return query.getResultList();
    }

    //Buscar lista de Vendas pela data e cliente
    public List<Venda> comprasPelaDataCliente(LocalDate filtrarData, ClientePF cliente) {
        String hql = "from Venda as v where v.localDate = :filtrarData and v.cliente = :cliente ";
        Query query = em.createQuery(hql, Venda.class);
        query.setParameter("filtrarData", filtrarData);
        query.setParameter("cliente", cliente);
        return query.getResultList();
    }

    //Buscar lista de Compras de um Cliente
    public List<Venda> comprasCliente(ClientePF clientePF) {
        String hql = "from Venda as v where v.cliente like :clientePF";
        Query query = em.createQuery(hql, Venda.class);
        query.setParameter("clientePF", clientePF);
        return query.getResultList();
    }

    //Busca lista de Compras de vários Clientes
    public List<Venda> comprasListClientes(String clientePF) {
        String hql = "from Venda as v where v.cliente.nome like :nome";
        TypedQuery<Venda> query = em.createQuery(hql, Venda.class);
        query.setParameter("nome", "%"+clientePF+"%");
        return query.getResultList();
    }

}
