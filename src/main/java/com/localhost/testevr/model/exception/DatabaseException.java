/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.localhost.testevr.model.exception;

import java.io.Serializable;

/**
 * Representa uma exceção que possa ser lançada pelo manipulador do banco de
 * dados
 *
 * @author Lucas
 */
public class DatabaseException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public DatabaseException(String string) {
        super(string);
    }

    public DatabaseException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

}
