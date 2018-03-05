package br.com.boletos.controller;

import br.com.boletos.model.persistence.DAO;

import java.lang.reflect.ParameterizedType;

public class DefaultController<T> {

    DAO<T> service;

    /**
     * Classe negérica que usa reflexao do java para injetar serviços
     * Não sendo necessário utilizar neste projeto, devido a pouco complexidade
     *
     * @return
     */
    public DAO<T> gerService() {
        if (this.service != null)
            return this.service;
        try {
            this.service = (DAO<T>) Class.forName((((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName()).replace("model", "service") + "Service").newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return this.service;
    }
}
