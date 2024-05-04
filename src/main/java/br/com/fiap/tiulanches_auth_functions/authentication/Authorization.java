package br.com.fiap.tiulanches_auth_functions.authentication;

import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.HttpRequestMessage;

public class Authorization {

    public String getAuthorization(HttpRequestMessage<Optional<String>>req) {
        Map<String, String> headers = req.getHeaders();
        
        return headers.get("authorization");
    }  

    public LoginCliente getLoginCliente(String response) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response, LoginCliente.class);
    }
}
