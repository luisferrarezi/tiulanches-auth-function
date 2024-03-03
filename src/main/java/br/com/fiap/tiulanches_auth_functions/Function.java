package br.com.fiap.tiulanches_auth_functions;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;


public class Function {
    @SuppressWarnings("unused")
    @FunctionName("RegistraLoginCliente")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        Authorization auth = new Authorization();
        auth.getAuthorization(request);

        try {
            URL url = new URL("https://graph.microsoft.com/v1.0/me");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", auth.getAccessToken());
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
                return request.createResponseBuilder(HttpStatus.OK).body(response.toString()).build();
            } else if(httpResponseCode == HTTPResponse.SC_UNAUTHORIZED) {                
                return request.createResponseBuilder(HttpStatus.UNAUTHORIZED).body(response.toString()).build();
            } else {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body(response.toString()).build();
            }   
        } catch (Exception e) {
            return request.createResponseBuilder(HttpStatus.FORBIDDEN).body(e.getMessage()).build();
        }        

        /*String updateQuery = "UPDATE clientes SET LOGADO = ? WHERE email = ?";
        String connString = criaConnString();
        
        try (Connection connection = DriverManager.getConnection(connString);                
             PreparedStatement updateClienteLogado = connection.prepareStatement(updateQuery)) {

            updateClienteLogado.setInt(1, auth.getLogin());
            updateClienteLogado.setString(2, auth.getEmail());
            updateClienteLogado.executeUpdate();
            
            return request.createResponseBuilder(HttpStatus.OK).body("Usuário logado!").build();
        } catch (SQLException ex) {
            context.getLogger().severe("Erro de conexão com a base: " + ex.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }*/
    }

    private String criaConnString(){
        return System.getenv("DATASOURCE_URL") + 
                "&user=" + System.getenv("DATASOURCE_USERNAME") + 
                "&password=" + System.getenv("DATASOURCE_PASSWORD");
    }
}
