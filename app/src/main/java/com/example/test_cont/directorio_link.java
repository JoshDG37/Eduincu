package com.example.test_cont;

public class directorio_link {
    private String carpetaId;
    private String nombreCarpeta;
    private String linkCarpeta;

    public directorio_link() {
    }

    public directorio_link(String carpetaId, String nombreCarpeta, String linkCarpeta) {
        this.carpetaId = carpetaId;
        this.nombreCarpeta = nombreCarpeta;
        this.linkCarpeta = linkCarpeta;
    }

    public String getCarpetaId() {
        return carpetaId;
    }

    public void setCarpetaId(String carpetaId) {
        this.carpetaId = carpetaId;
    }

    public String getNombreCarpeta() {
        return nombreCarpeta;
    }

    public void setNombreCarpeta(String nombreCarpeta) {
        this.nombreCarpeta = nombreCarpeta;
    }

    public String getLinkCarpeta() {
        return linkCarpeta;
    }

    public void setLinkCarpeta(String linkCarpeta) {
        this.linkCarpeta = linkCarpeta;
    }
}
