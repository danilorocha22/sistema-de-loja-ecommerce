package com.loja.danilo.repositorys;

import com.loja.danilo.models.Role;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author danilo
 */
@SuppressWarnings("all")
@Repository
public class RoleRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Role role) {
        em.persist(role);
    }

    public Role find(Long id) {
        return em.find(Role.class, id);
    }
}
