package com.localhost.testevr.services;

import com.localhost.testevr.data.dao.CursoDAO;
import com.localhost.testevr.model.entities.Curso;
import com.localhost.testevr.model.exception.DaoException;
import java.util.List;

/**
 * Controladora das operações sobre cursos
 *
 * @author Lucas
 */
public abstract class CursoController {

    /**
     * Cria um novo curso
     *
     * @param nome_curso O nome do novo curso
     * @param ementa_curso A ementa do novo curso
     * @return O curso criado
     * @throws DaoException Em caso de erros
     */
    public synchronized static Curso novoCurso(String nome_curso, String ementa_curso) {
        assert nome_curso != null : "Nome do curso invalido";
        Curso curso = new Curso(null, nome_curso, ementa_curso);

        return CursoDAO.criar(curso).orElseThrow(() -> new DaoException("Ocorreu um erro ao criar o curso"));
    }

    /**
     * Atualiza um curso existente
     *
     * @param codigo_curso O código do curso a ser atualizado
     * @param nome_curso O nome do curso a ser atualizado
     * @param ementa_curso A ementa do curso a ser atualizado
     * @return Verdadeiro em caso de sucesso, falso ao contrário
     */
    public synchronized static boolean atualizaCurso(Integer codigo_curso, String nome_curso, String ementa_curso) {
        assert codigo_curso != null : "Codigo do curso invalido";
        assert nome_curso != null : "Nome do curso invalido";
        assert ementa_curso != null : "Nome do curso invalido";
        Curso curso = new Curso(codigo_curso, nome_curso, ementa_curso);

        return CursoDAO.atualizar(curso);
    }

    /**
     * Exclui um curso
     *
     * @param codigo_curso O código do curso a ser excluido
     * @return Verdadeiro em caso de sucesso, falso ao contrário
     */
    public synchronized static boolean excluirCurso(Integer codigo_curso) {
        assert codigo_curso != null : "Codigo do curso invalido";

        return CursoDAO.excluirPorId(codigo_curso);
    }

    /**
     * Busca um curso pelo código
     *
     * @param codigo_curso O código do curso a ser encontrado
     * @return O curso encontrado
     * @throws DaoException Caso o curso não tenha sido encontrado
     */
    public synchronized static Curso buscarCursoPorCodigo(Integer codigo_curso) {
        assert codigo_curso != null : "Codigo do curso invalido";

        return CursoDAO.buscaPorId(codigo_curso)
                .orElseThrow(() -> new DaoException("Ocorreu um erro ao criar o curso"));
    }

    /**
     * Busca todos os cursos no sistema
     *
     * @return A lista de cursos
     */
    public synchronized static List<Curso> buscaTodos() {
        return CursoDAO.buscaTodos();
    }

    /**
     * Carrega a lista de alunos no curso especificado
     *
     * @param curso O curso a ser carregado
     * @return O curso com a lista de alunos preenchida
     */
    public synchronized static Curso carregaAlunosCurso(Curso curso) {
        return CursoDAO.carregaAlunosCurso(curso)
                .orElseThrow(() -> new DaoException("Ocorreu um erro ao obter a lista de alunos do curso"));
    }

    /**
     * Busca todos os cursos no qual o aluno especificado não esteja matriculado
     *
     * @param codigo_aluno O código do aluno
     * @return A lista de cursos
     */
    public synchronized static List<Curso> buscaTodosCursosSemAluno(Integer codigo_aluno) {
        return CursoDAO.buscaTodosCursosSemAluno(codigo_aluno);
    }
}
