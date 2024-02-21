package br.com.fiap.tiulanches_auth_functions;

import java.util.Map;
import java.util.Optional;

import com.microsoft.azure.functions.HttpRequestMessage;

import lombok.Getter;

public class Authorization {

    @Getter    
    private String email;
    
    @Getter
    private int login;

    public Authorization getAuthorization(HttpRequestMessage<Optional<String>>req) {
        Map<String, String> headers = req.getHeaders();
        this.email = headers.get("email");
        this.login = Integer.parseInt(headers.get("login"));
        return this;
    }  
}
