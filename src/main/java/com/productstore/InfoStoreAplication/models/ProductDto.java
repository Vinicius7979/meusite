package com.productstore.InfoStoreAplication.models;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public class ProductDto {

    @NotEmpty(message = "Nome necessário!")
    private String nome;

    @NotEmpty(message = "Marca necessária!")
    private String marca;

    @NotEmpty(message = "Categoria ncessária!")
    private String categoria;

    @Min(0)
    private double preco;

    @Size(min = 10, message = "Descrição minima de 10 caracteres")
    @Size(max = 2000, message = "Descrição máxima de 2000 caracteres")
    private String descricao;

    private MultipartFile imageFileName;

    public @NotEmpty(message = "Nome necessário!") String getNome() {
        return nome;
    }

    public void setNome(@NotEmpty(message = "Nome necessário!") String nome) {
        this.nome = nome;
    }

    public @NotEmpty(message = "Marca necessária!") String getMarca() {
        return marca;
    }

    public void setMarca(@NotEmpty(message = "Marca necessária!") String marca) {
        this.marca = marca;
    }

    public @NotEmpty(message = "Categoria ncessária!") String getCategoria() {
        return categoria;
    }

    public void setCategoria(@NotEmpty(message = "Categoria ncessária!") String categoria) {
        this.categoria = categoria;
    }

    @Min(0)
    public double getPreco() {
        return preco;
    }

    public void setPreco(@Min(0) double preco) {
        this.preco = preco;
    }

    public @Size(min = 10, message = "Descrição minima de 10 caracteres") @Size(max = 2000, message = "Descrição máxima de 2000 caracteres") String getDescricao() {
        return descricao;
    }

    public void setDescricao(@Size(min = 10, message = "Descrição minima de 10 caracteres") @Size(max = 2000, message = "Descrição máxima de 2000 caracteres") String descricao) {
        this.descricao = descricao;
    }

    public MultipartFile getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(MultipartFile imageFileName) {
        this.imageFileName = imageFileName;
    }

}
