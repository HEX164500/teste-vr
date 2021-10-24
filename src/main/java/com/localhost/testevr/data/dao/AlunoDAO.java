package com.localhost.testevr.data.dao;

import com.localhost.testevr.configuration.DatabaseConnector;
import com.localhost.testevr.model.entities.Aluno;
import com.localhost.testevr.model.entities.Curso;
import com.localhost.testevr.model.exception.DaoException;
import com.localhost.testevr.model.exception.DatabaseException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe de manipulação de banco da entidade Aluno
 *
 * @author Lucas
 * @version 0.1
 */
public class AlunoDAO {

    /**
     * Método que salva um aluno no banco de dados
     *
     * @param aluno O aluno a ser salvo
     * @return O aluno caso exista
     */
    public static Optional<Aluno> criar(Aluno aluno) {
        final String SQL = "INSERT INTO aluno(nome) VALUES (?) RETURNING codigo AS codigo_aluno;";

        try {
            PreparedStatement query = DatabaseConnector.getConnection().prepareStatement(SQL);
            query.setString(1, aluno.getNome());

            try (ResultSet rs = query.executeQuery()) {
                if (rs.next()) {
                    aluno.setCodigo(rs.getInt("codigo_aluno"));
                    return Optional.of(aluno);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao salvar aluno", e);
        }
    }

    /**
     * Atualiza um aluno no banco de dados
     *
     * @param aluno O aluno a ser atualizado
     * @return O aluno atualizado
     */
    public static boolean atualizar(Aluno aluno) {
        final String SQL = "UPDATE aluno SET nome = ? WHERE codigo = ?;";

        try {
            PreparedStatement query = DatabaseConnector.getConnection().prepareStatement(SQL);
            query.setString(1, aluno.getNome());
            query.setInt(2, aluno.getCodigo());

            return query.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao atualizar aluno", e);
        }
    }

    /**
     * Excluir um aluno dos registros, por não ser especificado a desativação
     * foi considerada a exclusão real do registro
     *
     * @param codigo Codigo do aluno a ser excluido
     * @return Verdadeiro caso o aluno tenha sido excluido, falso caso contrário
     */
    public static boolean excluirPorId(int codigo) {
        final String SQL_EXCLUIR_CURSO_ALUNO = "DELETE FROM curso_aluno WHERE codigo_aluno = ?;";
        final String SQL_EXCLUIR_ALUNO = "DELETE FROM aluno WHERE codigo = ?;";

        try {
            DatabaseConnector.getConnection().setAutoCommit(false);

            PreparedStatement query = DatabaseConnector.getConnection().prepareStatement(SQL_EXCLUIR_ALUNO);
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
                throw new DatabaseException("Erro ao desfazer exclusão do aluno, "
                        + "após um erro na exclusão acontecer", e2);
            }

            throw new DaoException("Erro ao excluir aluno", e);

        }
    }

    /**
     * Busca um aluno pelo código
     *
     * @param codigo O código a ser encontrado
     * @return O aluno caso encontrado
     */
    public static Optional<Aluno> buscaPorId(int codigo) {
        final String SQL = "SELECT codigo, nome FROM aluno WHERE codigo = ?;";

        try {
            PreparedStatement query = DatabaseConnector.getConnection().prepareStatement(SQL);
            query.setInt(1, codigo);

            try (ResultSet rs = query.executeQuery()) {
                if (rs.next()) {
                    Aluno obj = new Aluno(rs.getInt("codigo"), rs.getString("nome"));
                    return Optional.of(obj);
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar aluno", e);
        }
    }

    /**
     * Busca todos os alunos no banco
     *
     * @return A lista de alunos
     */
    public static List<Aluno> buscaTodos() {
        final String SQL = "SELECT codigo, nome FROM aluno";

        List<Aluno> alunos = new ArrayList(20);

        try {
            Statement query = DatabaseConnector.getConnection().createStatement();

            try (ResultSet rs = query.executeQuery(SQL)) {
                while (rs.next()) {
                    Aluno obj = new Aluno(rs.getInt("codigo"), rs.getString("nome"));
                    alunos.add(obj);
                }
            }
            return alunos;
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar alunos", e);
        }
    }

    /**
     * Busca todos os alunos no banco com nome informado que contenha o nome
     * fornecido
     *
     * @param nome O nome a ser buscado
     * @return A lista de alunos
     */
    public static List<Aluno> buscaPorNomeContendo(String nome) {
        final String SQL = "SELECT codigo, nome FROM aluno WHERE nome ilike ?";

        List<Aluno> alunos = new ArrayList(20);

        try {
            PreparedStatement query = DatabaseConnector.getConnection().prepareStatement(SQL);

            query.setString(1, "%" + nome + "%");

            try (ResultSet rs = query.executeQuery()) {
                while (rs.next()) {
                    Aluno obj = new Aluno(rs.getInt("codigo"), rs.getString("nome"));
                    alunos.add(obj);
                }
            }
            return alunos;
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar alunos", e);
        }
    }

    /**
     * Busca todos os alunos que estejam em um determinado curso
     *
     * @param curso
     * @return
     */
    public static List<Aluno> buscaAlunosPorCurso(Curso curso) {
        final String SQL = "SELECT a.codigo, a.nome FROM aluno a JOIN curso_aluno cs ON a.codigo = cs.codigo_aluno "
                + "AND cs.codigo_curso = ?";

        List<Aluno> alunos = new ArrayList(20);

        try {
            PreparedStatement query = DatabaseConnector.getConnection().prepareCall(SQL);

            query.setInt(1, curso.getCodigo());

            try (ResultSet rs = query.executeQuery()) {
                while (rs.next()) {
                    Aluno obj = new Aluno(rs.getInt("codigo"), rs.getString("nome"));
                    alunos.add(obj);
                }
            }
            return alunos;
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar alunos do curso especificado", e);
        }
    }

    /**
     * Carrega os cursos associados no aluno especificado e o retorna
     *
     * @param codigo_aluno O aluno a ser carregado
     * @return O aluno carregado
     */
    public static Optional<Aluno> carregaCursosAluno(Integer codigo_aluno) {
        final String SQL_CURSOS = "SELECT c.codigo, c.descricao, c.ementa FROM curso c JOIN curso_aluno cs "
                + "ON c.codigo = cs.codigo_curso WHERE cs.codigo_aluno = ?";

        List<Curso> cursos = new ArrayList<>(20);

        try {
            Aluno aluno = buscaPorId(codigo_aluno).orElseThrow(() -> new DaoException("Não foi possível localizar o aluno"));

            PreparedStatement query = DatabaseConnector.getConnection().prepareCall(SQL_CURSOS);

            query.setInt(1, codigo_aluno);

            try (ResultSet rs = query.executeQuery()) {
                while (rs.next()) {
                    Curso obj = new Curso(rs.getInt("codigo"), rs.getString("descricao"), rs.getString("ementa"));
                    cursos.add(obj);
                }
            }
            aluno.setCursos(cursos);
            return Optional.of(aluno);
        } catch (SQLException e) {
            throw new DaoException("Erro ao carregar cursos do aluno", e);
        }
    }

    /**
     * Desassocia o aluno do curso especificado
     *
     * @param codigo_aluno O código do aluno
     * @param codigo_curso O Código do curso
     * @return Verdadeiro caso seja removido, falso ao contrário
     * @throws DatabaseException Em caso de erros
     */
    public static boolean removerCursoAluno(Integer codigo_aluno, Integer codigo_curso) {
        final String SQL = "DELETE FROM curso_aluno WHERE codigo_aluno = ? AND codigo_curso = ?;";

        try {
            PreparedStatement query = DatabaseConnector.getConnection().prepareCall(SQL);

            query.setInt(1, codigo_aluno);
            query.setInt(2, codigo_curso);

            return query.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao remover aluno do curso", e);
        }
    }

    /**
     * Adicionar aluno ao curso especificado
     *
     * @param codigo_aluno O código do aluno
     * @param codigo_curso O código do curso
     * @return Verdadeiro em caso de sucesso, falso ao contrário
     */
    public static boolean adicionarCursoAluno(Integer codigo_aluno, Integer codigo_curso) {
        final String SQL = "INSERT INTO curso_aluno (codigo_aluno, codigo_curso) VALUES(?, ?)";

        try {
            PreparedStatement query = DatabaseConnector.getConnection().prepareCall(SQL);

            query.setInt(1, codigo_aluno);
            query.setInt(2, codigo_curso);

            return query.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao adicionar aluno ao curso", e);
        }
    }
}
