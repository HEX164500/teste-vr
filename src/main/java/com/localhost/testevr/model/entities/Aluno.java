package com.localhost.testevr.model.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidade que representa um aluno
 *
 * @author Lucas
 */
public final class Aluno {

    private Integer codigo;
    private String nome;
    private final Set<Curso> cursos = new HashSet<>();

    public Aluno() {

    }

    /**
     * @param codigo Codigo do aluno
     * @param nome Nome do aluno
     */
    public Aluno(Integer codigo, String nome) {
        setCodigo(codigo);
        setNome(nome);
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    /**
     * Seta o nome do aluno
     *
     * @param nome O nome do aluno
     * @throws IllegalArgumentException Caso o nome do aluno seja nulo ou tenha mais que
     * 50 caracteres
     */
    public void setNome(String nome) {
        if (nome == null) {
            throw new IllegalArgumentException("Nome do aluno deve ser informado");
        }
        if (nome.length() > 50 || nome.isEmpty()) {
            throw new IllegalArgumentException("Nome do aluno deve ter até 50 caracteres e não pode estar vazio");
        }

        this.nome = nome;
    }

    /**
     * Adiciona os curso ao aluno
     *
     * @param cursos A coleção de cursos
     * @throws AssertionError Caso a coleção seja nula
     */
    public void setCursos(Collection<? extends Curso> cursos) {
        if (cursos == null) {
            throw new IllegalArgumentException("A lista de cursos não pode ser nula");
        }
        this.cursos.addAll(cursos);
    }

    public Set<Curso> getCursos() {
        return cursos;
    }

    /**
     * Obtem o Hash Code a partir do código do aluno
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.codigo;
        return hash;
    }

    /**
     * Compara o objeto com a instancia fornecida verificando o código
     *
     * @param obj
     * @return Verdadeiro caso seja o mesmo código, falso ao contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Aluno other = (Aluno) obj;
        if (this.codigo != other.codigo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Aluno{" + "codigo=" + codigo + ", nome=" + nome + '}';
    }
}
