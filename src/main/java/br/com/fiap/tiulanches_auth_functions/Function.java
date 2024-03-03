package br.com.fiap.tiulanches_auth_functions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;

import br.com.fiap.tiulanches_auth_functions.authentication.Authorization;
import br.com.fiap.tiulanches_auth_functions.authentication.LoginCliente;
import br.com.fiap.tiulanches_auth_functions.repository.DB;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;

public class Function {

    @SuppressWarnings("deprecation")
    @FunctionName("LoginCliente")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        try {
            URL url = new URL("https://graph.microsoft.com/v1.0/me");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    
            Authorization auth = new Authorization();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", auth.getAuthorization(request));
            conn.setRequestProperty("Accept","application/json");

            StringBuilder response;            
            int httpResponseCode = conn.getResponseCode();

            try(BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))){

                String inputLine;
                response = new StringBuilder();
                while (( inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }                            

            if(httpResponseCode == HTTPResponse.SC_OK) {
                LoginCliente loginCliente = auth.getLoginCliente(response.toString());

                DB db = new DB();
                db.informaClienteAutenticado(loginCliente.getMail());

                return request.createResponseBuilder(HttpStatus.OK).body(response.toString()).build();
            } else if(httpResponseCode == HTTPResponse.SC_UNAUTHORIZED) {                
                return request.createResponseBuilder(HttpStatus.UNAUTHORIZED).body(response.toString()).build();
            } else {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body(response.toString()).build();
            }           
        } catch (Exception e) {
            if ((e instanceof SQLException) || (e instanceof JsonProcessingException) || (e instanceof JsonMappingException)) {
                return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao registrar login cliente!").build();
            } else {
                return request.createResponseBuilder(HttpStatus.FORBIDDEN).body(e.getMessage()).build();
            }
        }
    }
}
