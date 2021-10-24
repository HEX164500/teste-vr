/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.localhost.testevr.data.dao;

import com.localhost.testevr.configuration.DatabaseConnector;
import com.localhost.testevr.model.entities.Aluno;
import com.localhost.testevr.model.entities.Curso;
import com.localhost.testevr.model.exception.DatabaseException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe de manipulação de registros de cursos
 *
 * @author Lucas
 */
public class CursoDAO {

    /**
     * Salva um curso no banco de dados
     *
     * @param curso O curso a ser salvo
     * @return O curso salvo
     */
    public static Optional<Curso> criar(Curso curso) {
        final String SQL = "INSERT INTO curso(descricao, ementa) VALUES (?, ?) RETURNING codigo AS codigo_curso;";

        try {
            PreparedStatement query = DatabaseConnector.getConnection().prepareStatement(SQL);
            query.setString(1, curso.getDescricao());
            query.setString(2, curso.getEmenta());

            try (ResultSet rs = query.executeQuery()) {
                if (rs.next()) {
                    curso.setCodigo(rs.getInt("codigo_curso"));
                    return Optional.of(curso);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar curso", e);
        }
    }

    /**
     * Atualiza um curso no banco de dados
     *
     * @param curso O curso a ser atualizado
     * @return O curso atualizado
     */
    public static boolean atualizar(Curso curso) {
        final String SQL = "UPDATE curso SET descricao = ?, ementa = ? WHERE codigo = ?;";

        try {
            PreparedStatement query = DatabaseConnector.getConnection().prepareStatement(SQL);
            query.setString(1, curso.getDescricao());
            query.setString(2, curso.getEmenta());
            query.setInt(3, curso.getCodigo());

            return query.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar curso", e);
        }
    }

    /**
     * Exclui um curso e as relações de alunos matriculados
     *
     * @param codigo Codigo do curso a ser excluido
     * @return Verdadeiro em caso de sucesso, falso ao contrário
     */
    public static boolean excluirPorId(int codigo) {
        final String SQL_EXCLUIR_CURSO_ALUNO = "DELETE FROM curso_aluno WHERE codigo_curso = ?;";
        final String SQL_EXCLUIR_CURSO = "DELETE FROM curso WHERE codigo = ?;";

        try {
            DatabaseConnector.getConnection().setAutoCommit(false);

            PreparedStatement query = DatabaseConnector.getConnection().prepareStatement(SQL_EXCLUIR_CURSO);
            query.setInt(1, codigo);

            boolean alunoExcluido = query.executeUpdate() > 0;

            query = DatabaseConnector.getConnection().prepareStatement(SQL_EXCLUIR_CURSO_ALUNO);
            query.setInt(1, codigo);
            query.executeUpdate();

            DatabaseConnector.getConnection().commit();

            return alunoExcluido;
        } catch (SQLException e) {
            try {
                DatabaseConnector.getConnection().rollback();
            } catch (SQLException e2) {
                throw new DatabaseException("Erro ao desfazer exclusão do curso, "
                        + "após um erro na exclusão acontecer", e2);
            }

            throw new DatabaseException("Erro ao excluir curso", e);

        }
    }

    /**
     * Busca todos os cursos no sistema
     *
     * @return A lista de cursos
     */
    public static List<Curso> buscaTodos() {
        final String SQL = "SELECT codigo, descricao, ementa FROM curso";

        List<Curso> cursos = new ArrayList<>();

        try {
            Statement query = DatabaseConnector.getConnection().createStatement();

            try (ResultSet rs = query.executeQuery(SQL)) {
                while (rs.next()) {
                    Curso obj = new Curso(rs.getInt("codigo"), rs.getString("descricao"), rs.getString("ementa"));
                    cursos.add(obj);
                }
            }
            return cursos;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar cursos", e);
        }
    }

    /**
     * Busca todos os cursos que não contenham o aluno especificado
     *
     * @param codigo_aluno O código do aluno
     * @return A lista de cursos
     */
    public static List<Curso> buscaTodosCursosSemAluno(Integer codigo_aluno) {
        final String SQL
                = "SELECT c.* FROM curso c WHERE codigo NOT IN ("
                + " SELECT codigo_curso FROM curso_aluno WHERE codigo_aluno = ?"
                + ")";

        List<Curso> cursos = new ArrayList<>();

        try {
            PreparedStatement query = DatabaseConnector.getConnection().prepareStatement(SQL);

            query.setInt(1, codigo_aluno);

            try (ResultSet rs = query.executeQuery()) {
                while (rs.next()) {
                    Curso obj = new Curso(rs.getInt("codigo"), rs.getString("descricao"), rs.getString("ementa"));
                    cursos.add(obj);
                }
            }
            return cursos;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar cursos", e);
        }
    }

    /**
     * Busca um curso pelo codigo
     *
     * @param codigo O código do curso
     * @return O curso encontrado
     */
    public static Optional<Curso> buscaPorId(int codigo) {
        final String SQL = "SELECT codigo, descricao, ementa FROM aluno WHERE codigo = ?;";

        try {
            PreparedStatement query = DatabaseConnector.getConnection().prepareStatement(SQL);
            query.setInt(1, codigo);

            try (ResultSet rs = query.executeQuery()) {
                if (rs.next()) {
                    Curso obj = new Curso(rs.getInt("codigo"), rs.getString("descricao"), rs.getString("ementa"));
                    return Optional.of(obj);
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar curso", e);
        }
    }

    /**
     * Busca todos os cursos que tenham um determinado texto na descrição
     *
     * @param descricao A descrição a ser buscada
     * @return Os cursos encontrados
     */
    public static List<Curso> buscaPorDescricaoContendo(String descricao) {
        final String SQL = "SELECT codigo, descricao, ementa FROM curso WHERE descricao ilike ?";

        List<Curso> cursos = new ArrayList<>(20);

        try {
            PreparedStatement query = DatabaseConnector.getConnection().prepareCall(SQL);

            query.setString(1, "%" + descricao + "%");

            try (ResultSet rs = query.executeQuery(SQL)) {
                while (rs.next()) {
                    Curso obj = new Curso(rs.getInt("codigo"), rs.getString("descricao"), rs.getString("ementa"));
                    cursos.add(obj);
                }
            }
            return cursos;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar cursos", e);
        }
    }

    /**
     * Carrega a lista de alunos do curso especificado
     *
     * @param curso O curso a ser carregado
     * @return O curso com a lista de alunos carregados
     */
    public static Optional<Curso> carregaAlunosCurso(Curso curso) {
        final String SQL = "SELECT a.codigo, a.nome FROM aluno a JOIN curso_aluno cs "
                + "ON a.codigo = cs.codigo_aluno WHERE cs.codigo_curso = ?";

        List<Aluno> alunos = new ArrayList<>(20);

        try {
            PreparedStatement query = DatabaseConnector.getConnection().prepareCall(SQL);

            query.setInt(1, curso.getCodigo());

            try (ResultSet rs = query.executeQuery()) {
                while (rs.next()) {
                    Aluno obj = new Aluno(rs.getInt("codigo"), rs.getString("nome"));
                    alunos.add(obj);
                }
            }
            curso.setAlunos(alunos);
            return Optional.of(curso);
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao carregar alunos do curso", e);
        }
    }
}
