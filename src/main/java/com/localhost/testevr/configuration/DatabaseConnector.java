package com.localhost.testevr.configuration;

import com.localhost.testevr.model.exception.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe para manipulação da conexão com o banco de dados
 *
 * @author Lucas
 */
public abstract class DatabaseConnector {

    /**
     * Notas: Esse banco é um dyno free do Heroku que peguei emprestado para
     * este teste
     */
    private static final String HOSTNAME = "ec2-34-232-245-127.compute-1.amazonaws.com";
    private static final String HOSTPORT = "5432";
    private static final String DATABASE = "dabuq6o5sdvm58";
    private static final String USERNAME = "ebccanhisjgtek";
    private static final String PASSWORD = "a93e5c51b969a771b1f247f6b33b432994037f41b81c4c1bcbb2c7ccbb3e04c9";
    private static final boolean USE_SSL = false;

    private static Connection conexao;

    /**
     * Obetem uma conexão com o banco de dados previamente configurado
     *
     * @return A conexão criada
     * @throws DatabaseException caso ocorra algum erro durante a conexão com o
     * banco de dados
     */
    public static Connection getConnection() {
        final String DSN = "jdbc:postgresql://" + HOSTNAME + ":" + HOSTPORT + "/" + DATABASE + "?ssl=" + USE_SSL;

        try {
            if (conexao != null) {
                if (!conexao.isClosed()) {
                    return conexao;
                }
            }
            conexao = DriverManager.getConnection(DSN, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao estabelecer conexão com o banco de dados", e);
        }
        return conexao;
    }
}
