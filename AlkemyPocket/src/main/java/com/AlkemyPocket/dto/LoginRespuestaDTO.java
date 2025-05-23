package com.AlkemyPocket.dto;

import com.AlkemyPocket.model.Usuario;

public class LoginRespuestaDTO {
    private String token;
    private Usuario usuario;

    public LoginRespuestaDTO(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
