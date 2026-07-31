package com.sierra_dorada.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "categorias")
public class Categoria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer id;
    @NotBlank @Size(max = 50)
    private String nombre;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_principal_id")
    private Categoria categoriaPrincipal;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Categoria getCategoriaPrincipal() { return categoriaPrincipal; }
    public void setCategoriaPrincipal(Categoria categoriaPrincipal) { this.categoriaPrincipal = categoriaPrincipal; }
}
