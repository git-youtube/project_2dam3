package com.example.reto;

public class Incidencias {
    private String id;
    private String tipo;
    private String causa;
    private String ciudad;
    private String nivel;
    private String carretera;

    // Constructor
    public Incidencias(String id, String tipo, String causa, String ciudad, String nivel, String carretera) {
        this.id = id;
        this.tipo = tipo;
        this.causa = causa;
        this.ciudad = ciudad;
        this.nivel = nivel;
        this.carretera = carretera;
    }

    public Incidencias(String tipo, String causa, String ciudad, String nivel, String carretera) {
        this.tipo = tipo;
        this.causa = causa;
        this.ciudad = ciudad;
        this.nivel = nivel;
        this.carretera = carretera;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCausa() {
        return causa;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getNivel() {
        return nivel;
    }

    public String getCarretera() {
        return carretera;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public void setCarretera(String carretera) {
        this.carretera = carretera;
    }
}

