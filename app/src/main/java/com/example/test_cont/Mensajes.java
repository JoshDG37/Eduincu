package com.example.test_cont;

public class Mensajes {
    private String from, mensaje, type;

    public Mensajes(){}

    public Mensajes(String from, String mensaje, String type) {
        this.from = from;
        this.mensaje = mensaje;
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
