package br.com.fiap.tiulanches_auth_functions.authentication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginCliente {
    
    private String mail;
    private String displayName;
}
