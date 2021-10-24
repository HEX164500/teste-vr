package com.localhost.testevr.services;

import com.localhost.testevr.data.dao.AlunoDAO;
import com.localhost.testevr.model.entities.Aluno;
import com.localhost.testevr.model.exception.DaoException;
import java.util.List;

/**
 * Controladora das operações sobre alunos
 *
 * @author Lucas
 */
public abstract class AlunoController {

    /**
     * Cria um novo aluno
     *
     * @param nome_aluno O nome do novo aluno
     * @return O aluno criado
     * @throws DaoException Em caso de erros
     */
    public synchronized static Aluno novoAluno(String nome_aluno) {
        assert nome_aluno != null : "Nome do aluno invalido";
        Aluno aluno = new Aluno(null, nome_aluno);

        return AlunoDAO.criar(aluno).orElseThrow(() -> new DaoException("Ocorreu um erro ao criar o aluno"));
    }

    /**
     * Atualiza um aluno existente
     *
     * @param codigo_aluno O código do aluno a ser atualizado
     * @param nome_aluno O nome do aluno a ser atualizado
     * @return Verdadeiro em caso de sucesso, falso ao contrário
     */
    public synchronized static boolean atualizaAluno(Integer codigo_aluno, String nome_aluno) {
        assert codigo_aluno != null : "Codigo do aluno invalido";
        assert nome_aluno != null : "Nome do aluno invalido";
        Aluno aluno = new Aluno(codigo_aluno, nome_aluno);

        return AlunoDAO.atualizar(aluno);
    }

    /**
     * Exclui um aluno
     *
     * @param codigo_aluno O código do aluno a ser excluido
     * @return Verdadeiro em caso de sucesso, falso ao contrário
     */
    public synchronized static boolean excluirAluno(Integer codigo_aluno) {
        assert codigo_aluno != null : "Codigo do aluno invalido";

        return AlunoDAO.excluirPorId(codigo_aluno);
    }

    /**
     * Busca um aluno pelo código
     *
     * @param codigo_aluno O código do aluno a ser encontrado
     * @return O aluno encontrado
     * @throws DaoException Caso o aluno não tenha sido encontrado
     */
    public synchronized static Aluno buscarAlunoPorCodigo(Integer codigo_aluno) {
        assert codigo_aluno != null : "Codigo do aluno invalido";

        return AlunoDAO.buscaPorId(codigo_aluno)
                .orElseThrow(() -> new DaoException("Não foi possível localizar o aluno"));
    }

    /**
     * Busca todos os alunos no sistema
     *
     * @return A lista de alunos
     */
    public synchronized static List<Aluno> buscaTodos() {
        return AlunoDAO.buscaTodos();
    }

    /**
     * Busca todos os alunos que o nome conhecida com o nome fornecido
     *
     * @param nome O nome a ser procurado
     * @return A lista de alunos encontrados
     */
    public synchronized static List<Aluno> buscaPorNomeContendo(String nome) {
        return AlunoDAO.buscaPorNomeContendo(nome);
    }

    /**
     * Carrega todos os cursos do aluno especificado
     *
     * @param codigo_aluno O código do aluno
     * @return O aluno com os cursos carregados
     * @throws DaoException em caso de erros
     */
    public synchronized static Aluno carregarCursos(Integer codigo_aluno) {
        return AlunoDAO.carregaCursosAluno(codigo_aluno)
                .orElseThrow(() -> new DaoException("Ocorreu um erro ao localizar o aluno"));
    }

    /**
     * Remove o aluno do curso especificado
     *
     * @param codigo_aluno O código do aluno
     * @param codigo_curso O código do curso
     * @return Verdadeiro em caso de sucesso, falso ao contrário
     */
    public synchronized static boolean removerCursoAluno(Integer codigo_aluno, Integer codigo_curso) {
        return AlunoDAO.removerCursoAluno(codigo_aluno, codigo_curso);
    }

    /**
     * Adiciona o aluno especificado ao curso
     *
     * @param codigo_aluno O código do aluno
     * @param codigo_curso O código do curso
     * @return Verdadeiro em caso de sucesso, falso ao contrário
     */
    public synchronized static boolean adicionarCursoAluno(Integer codigo_aluno, Integer codigo_curso) {
        return AlunoDAO.adicionarCursoAluno(codigo_aluno, codigo_curso);
    }
}
