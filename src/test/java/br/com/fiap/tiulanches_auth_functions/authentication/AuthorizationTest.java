package br.com.fiap.tiulanches_auth_functions.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microsoft.azure.functions.HttpRequestMessage;

class AuthorizationTest {
    
    private Authorization authorization;
    private static final String RESPONSE = "{\"mail\":\"teste@gmail.com\",\"displayName\":\"teste\",\"nome\":\"testedenovo\"}";

    @BeforeEach
    void BeforeEach(){
        this.authorization = new Authorization();
    }

    @Test
    @SuppressWarnings("unchecked")
    void getAuthorizationTest(){        
        final HttpRequestMessage<Optional<String>> req = mock(HttpRequestMessage.class);
        
        final Map<String, String> headers = new HashMap<>();
        headers.put("authorization", "Bearer teste");
        doReturn(headers).when(req).getHeaders();

        assertEquals("Bearer teste", this.authorization.getAuthorization(req));
    }

    @Test
    void getLoginClienteTest() throws JsonProcessingException{
        LoginCliente loginCliente = this.authorization.getLoginCliente(RESPONSE);

        assertEquals("teste@gmail.com", loginCliente.getMail());
        assertEquals("teste", loginCliente.getDisplayName());
    }
}
