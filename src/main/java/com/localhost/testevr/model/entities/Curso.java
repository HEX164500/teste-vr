package com.localhost.testevr.model.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Lucas
 */
public class Curso {

    private Integer codigo;
    private String descricao;
    private String ementa;
    private final Set<Aluno> alunos = new HashSet<>();

    public Curso() {

    }

    public Curso(Integer codigo, String descricao, String ementa) {
        setCodigo(codigo);
        setDescricao(descricao);
        setEmenta(ementa);
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    /**
     * Seta a descrição do curso
     *
     * @param descricao A descrição do curso
     * @throws IllegalArgumentException Caso a descrição seja nula ou tenha mais
     * que 50 caracteres ou esteja vazia
     */
    public void setDescricao(String descricao) {

        if (descricao == null) {
            throw new IllegalArgumentException("Descrição do curso deve ser informado");
        }
        if (descricao.length() > 50 || descricao.isEmpty()) {
            throw new IllegalArgumentException("Descrição do curso deve ter até 50 caracteres e não pode estar vazia");
        }

        this.descricao = descricao;
    }

    public String getEmenta() {
        return ementa;
    }

    /**
     * Seta a ementa do curso
     *
     * @param ementa A ementa do curso
     * @throws AssertionError Caso a ementa seja nula
     */
    public void setEmenta(String ementa) {
        assert ementa != null : "Ementa do curso deve ser informado";
        this.ementa = ementa;
    }

    /**
     * Adiciona os alunos ao curso
     *
     * @param alunos Os alunos a serem adicionados
     * @throws AssertionError Caso a coleção seja nula
     */
    public void setAlunos(Collection<? extends Aluno> alunos) {
        assert alunos != null : "A lista de alunos não pode ser nula";
        this.alunos.addAll(alunos);
    }

    public Set<Aluno> getAlunos() {
        return alunos;
    }

    /**
     * Obtem o Hash Code a partir do código do curso
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.codigo;
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
        final Curso other = (Curso) obj;
        if (this.codigo != other.codigo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Curso{" + "codigo=" + codigo + ", descricao=" + descricao + ", ementa=" + ementa + '}';
    }
}
