package com.loja.danilo.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @author danilo
 */
@SuppressWarnings("all")
@Entity
@Table(name = "tb_itensVenda")
public class ItemVenda implements Serializable {
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Min(value = 1, message = " Por favor, informe a quantidade!")
    private int qtd;

    @JoinColumn(name = "id_produto")
    @OneToOne
    private Produto produto;

    @JoinColumn(name = "id_venda")
    @ManyToOne
    private Venda venda;

    //Método
    public double vlTotalItem() {
        return this.produto.getValor() * this.qtd;
    }

    //Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }
}
