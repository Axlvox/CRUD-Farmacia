package com.generation.farmacia.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(min = 5, max = 100, message = "O nome do produto deve conter no mínimo 05 caracteres, e no máximo 100")
    private String nome;

    @NotNull(message = "O preço do produto é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço do produto deve ser um número maior que zero")
    private BigDecimal price;

    @UpdateTimestamp
    private LocalDateTime data;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public BigDecimal calcularPrecoComDesconto(BigDecimal desconto) {
        BigDecimal percentualDesconto = desconto.divide(BigDecimal.valueOf(100));
        BigDecimal precoComDesconto = getPrice().multiply(BigDecimal.ONE.subtract(percentualDesconto));
        return precoComDesconto.setScale(2, RoundingMode.HALF_UP);
    }
}
