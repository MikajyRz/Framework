package com.utils;

import java.lang.reflect.Method;

public class MappingHandler {
    private Class<?> classe;
    private Method methode;

    // Constructeur par défaut sans initialisation
    public MappingHandler() {
    }

    // Constructeur qui enregistre la classe contrôleur et la méthode à appeler
    public MappingHandler(Class<?> classe, Method methode) {
        this.classe = classe;
        this.methode = methode;
    }

    // Retourne la classe contrôleur associée à l'URL
    public Class<?> getClasse() {
        return classe;
    }

    // Définit la classe contrôleur associée à l'URL
    public void setClasse(Class<?> classe) {
        this.classe = classe;
    }

    // Retourne la méthode du contrôleur associée à l'URL
    public Method getMethode() {
        return methode;
    }

    // Définit la méthode du contrôleur associée à l'URL
    public void setMethode(Method methode) {
        this.methode = methode;
    }
}
