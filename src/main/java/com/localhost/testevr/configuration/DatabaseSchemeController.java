package com.localhost.testevr.configuration;

import com.localhost.testevr.model.exception.DatabaseException;
import java.sql.SQLException;

/**
 * Controlador do schema da aplicação, responsável por gerar os esquemas
 * iniciais da aplicação
 *
 * @author Lucas
 */
public abstract class DatabaseSchemeController {

    /**
     * Cria todos as tabelas necessárias para o funcionamento do projeto
     */
    public static final void createSchemas() {
        try {
            createAlunoSchema();
            createCursoSchema();
            createCursoAlunoSchema();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao gerar esquemas da aplicação", e);
        }
    }

    /**
     * Cria o schema da tabela aluno
     *
     * @throws SQLException Em caso de falha ao criar a tabela
     */
    public static final void createAlunoSchema() throws SQLException {
        final String TABLE_SQL
                = "CREATE TABLE IF NOT EXISTS aluno("
                + " codigo SERIAL PRIMARY KEY, "
                + " nome VARCHAR(50)"
                + ");";

        DatabaseConnector.getConnection().createStatement().execute(TABLE_SQL);
    }

    /**
     * Cria o schema da tabela curso
     *
     * @throws SQLException Em caso de falha ao criar a tabela
     */
    public static final void createCursoSchema() throws SQLException {
        final String TABLE_SQL
                = "CREATE TABLE IF NOT EXISTS curso("
                + " codigo SERIAL PRIMARY KEY, "
                + " descricao VARCHAR(50),"
                + " ementa text"
                + ");";

        DatabaseConnector.getConnection().createStatement().execute(TABLE_SQL);
    }

    /**
     * Cria o schema da tabela curso_aluno
     *
     * @throws SQLException Em caso de falha ao criar a tabela
     */
    public static final void createCursoAlunoSchema() throws SQLException {
        final String TABLE_SQL
                = "CREATE TABLE IF NOT EXISTS curso_aluno("
                + " codigo SERIAL PRIMARY KEY, "
                + " codigo_curso INT NOT NULL,"
                + " codigo_aluno INT NOT NULL,"
                + " FOREIGN KEY (codigo_aluno) REFERENCES aluno(codigo),"
                + " FOREIGN KEY (codigo_curso) REFERENCES curso(codigo)"
                + ");";

        DatabaseConnector.getConnection().createStatement().execute(TABLE_SQL);
    }
}
