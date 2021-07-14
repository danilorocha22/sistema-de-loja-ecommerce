package com.loja.danilo.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author danilo
 */
@SuppressWarnings("all")
@Component
@Entity
@Scope("session")
@Table(name = "tb_vendas")
public class Venda implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate localDate = LocalDate.now();

    @ManyToOne
    private ClientePF cliente;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.PERSIST)
    private List<ItemVenda> itensVenda = new ArrayList<>();

    //Retorna o valor total da venda
    public double vlTotalVenda() {
        double vlTotal = 0;
        for(ItemVenda i : itensVenda) {
            vlTotal += i.vlTotalItem();
        }
        return vlTotal;
    }

    //Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public List<ItemVenda> getItensVenda() {
        return itensVenda;
    }

    public void setItensVenda(ItemVenda itensVenda) {
        this.itensVenda.add(itensVenda);
    }

    public ClientePF getCliente() {
        return cliente;
    }

    public void setCliente(ClientePF cliente) {
        this.cliente = cliente;
    }
}
