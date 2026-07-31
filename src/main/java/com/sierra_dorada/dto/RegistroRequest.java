package com.sierra_dorada.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record RegistroRequest(
    @NotBlank @Size(min=2,max=100) String nombre,
    @NotBlank @Size(min=2,max=100) String apellidos,
    @NotNull LocalDate fechaNacimiento,
    @NotBlank String genero,
    @NotBlank String direccion,
    @NotBlank @Pattern(regexp="^\\+?\\d{7,15}$") String telefono,
    @NotBlank @Email String email,
    @NotBlank @Size(min=6,max=100) String password
) {}
