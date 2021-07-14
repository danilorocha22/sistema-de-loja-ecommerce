package com.loja.danilo.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author danilo
 */
@SuppressWarnings("all")
@Table(name = "tb_role")
@Entity
public class Role implements GrantedAuthority {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String nome;

    @ManyToMany(mappedBy = "roles")
    private List<Usuario> usuario = new ArrayList<>();

    @Override
    public String getAuthority() {
        return nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Usuario> getUsuarios() {
        return usuario;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuario = usuarios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
