package com.utils;

import java.lang.reflect.Method;

public class MappingHandler {
    private Class<?> classe;
    private Method methode;

    public MappingHandler() {
    }

    public MappingHandler(Class<?> classe, Method methode) {
        this.classe = classe;
        this.methode = methode;
    }

    public Class<?> getClasse() {
        return classe;
    }

    public void setClasse(Class<?> classe) {
        this.classe = classe;
    }

    public Method getMethode() {
        return methode;
    }

    public void setMethode(Method methode) {
        this.methode = methode;
    }
}
