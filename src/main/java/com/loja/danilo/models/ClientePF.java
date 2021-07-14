package com.loja.danilo.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author danilo
 */
@SuppressWarnings("all")
@Entity
@Table(name = "tb_clientePF")
public class ClientePF extends Cliente implements Serializable {

    @NotBlank(message = " Informe o nome do cliente!")
    private String nome;

    @NotBlank(message = " Informe o CPF do cliente!")
    private String cpf;

    @OneToMany
    private List<Venda> vendas = new ArrayList<>();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public List<Venda> getVendas() {
        return vendas;
    }

    @Override
    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }
}
