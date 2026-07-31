package com.sierra_dorada.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer id;
    @NotBlank @Size(max = 200)
    @Column(name = "nombre_produ")
    private String nombre;
    @Column(name = "descripcion_produ", columnDefinition = "TEXT")
    private String descripcion;
    @NotNull @DecimalMin("0.0")
    @Column(name = "precio_base", precision = 12, scale = 2)
    private BigDecimal precio;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
    @Size(max = 100)
    private String marca;
    @Column(name = "tipo_cerveza")
    private String tipoCerveza;
    @Column(name = "estilo_cerveza")
    private String estiloCerveza;
    @Min(0)
    private Integer stock = 0;
    @DecimalMin("0.0")
    @Column(precision = 4, scale = 2)
    private BigDecimal abv;
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;
    private Boolean activo = true;

    @PrePersist
    void prePersist() {
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
        if (stock == null) stock = 0;
        if (activo == null) activo = true;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getTipoCerveza() { return tipoCerveza; }
    public void setTipoCerveza(String tipoCerveza) { this.tipoCerveza = tipoCerveza; }
    public String getEstiloCerveza() { return estiloCerveza; }
    public void setEstiloCerveza(String estiloCerveza) { this.estiloCerveza = estiloCerveza; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public BigDecimal getAbv() { return abv; }
    public void setAbv(BigDecimal abv) { this.abv = abv; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
