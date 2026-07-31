package com.sierra_dorada;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTests {
    @Autowired MockMvc mvc;

    @Test
    void registraUsuarioPermiteLoginYExponeCatalogoPublico() throws Exception {
        String registro = """
            {"nombre":"Ana","apellidos":"Cliente","fechaNacimiento":"1995-05-10",
             "genero":"Femenino","direccion":"Bogotá","telefono":"3001234567",
             "email":"ana@example.com","password":"secreto123"}
            """;
        mvc.perform(post("/registro").contentType(MediaType.APPLICATION_JSON).content(registro))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.token").isNotEmpty())
            .andExpect(jsonPath("$.rol").value("cliente"));

        String login = """
            {"usuario":"ana@example.com","password":"secreto123"}
            """;
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(login))
            .andExpect(status().isOk()).andExpect(jsonPath("$.tipo").value("Bearer"));

        mvc.perform(get("/productos")).andExpect(status().isOk());
    }

    @Test
    void rechazaLoginConCredencialesInvalidas() throws Exception {
        String login = """
            {"usuario":"nadie@example.com","password":"incorrecta"}
            """;
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(login))
            .andExpect(status().isUnauthorized());
    }
}
