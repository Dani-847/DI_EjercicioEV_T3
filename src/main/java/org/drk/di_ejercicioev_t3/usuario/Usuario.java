package org.drk.di_ejercicioev_t3.usuario;

import lombok.Data;

import java.util.Date;

@Data
public class Usuario {
    private String correo;
    private String plataforma;
    private Boolean administrador;
    private String version;
    private Date fechaCreacion;
}

