package com.loja.danilo.models;

import org.springframework.format.annotation.NumberFormat;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * @author danilo
 */
@SuppressWarnings("all")
@Entity
@Table(name = "tb_produtos")
public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotBlank(message = " Informe o nome do produto!")
    private String nome;

    @NotBlank(message = " Informe a descrição do produto!")
    private String descricao;

    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @DecimalMin(value = "0.01", message = " Valor mínimo do produto é R$ 0.01!")
    private double valor;

    //Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
