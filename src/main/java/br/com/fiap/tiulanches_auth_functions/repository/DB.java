package br.com.fiap.tiulanches_auth_functions.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DB {
    public void informaClienteAutenticado(String email) throws SQLException {
        String updateQuery = "UPDATE tlpedidos.clientes SET LOGADO = ? WHERE email = ?";
        String connString = criaConnString();
        
        Connection connection = DriverManager.getConnection(connString);                
        PreparedStatement updateClienteLogado = connection.prepareStatement(updateQuery);

        updateClienteLogado.setInt(1, 1);
        updateClienteLogado.setString(2, email);
        updateClienteLogado.executeUpdate();
    }

    private String criaConnString(){
        return System.getenv("DATASOURCE_URL") + 
                "&user=" + System.getenv("DATASOURCE_USERNAME") + 
                "&password=" + System.getenv("DATASOURCE_PASSWORD");
    }        
}
