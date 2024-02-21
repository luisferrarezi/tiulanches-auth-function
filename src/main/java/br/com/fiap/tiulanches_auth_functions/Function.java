package br.com.fiap.tiulanches_auth_functions;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

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

        String updateQuery = "UPDATE clientes SET LOGADO = ? WHERE email = ?";
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
        }
    }

    private String criaConnString(){
        return System.getenv("DATASOURCE_URL") + 
                "&user=" + System.getenv("DATASOURCE_USERNAME") + 
                "&password=" + System.getenv("DATASOURCE_PASSWORD");
    }
}
