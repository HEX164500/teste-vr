/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.localhost.testevr.model.exception;

import java.io.Serializable;

/**
 * Representa uma exceção que possa ser lançada pelo manipulador de entidades
 * DAO
 *
 * @author Lucas
 */
public class DaoException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public DaoException(String string) {
        super(string);
    }

    public DaoException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

}
