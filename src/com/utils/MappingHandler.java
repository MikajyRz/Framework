package com.utils;

import java.lang.reflect.Method;

import com.utils.UrlPattern;

public class MappingHandler {
    private Class<?> classe;
    private Method methode;
    private UrlPattern urlPattern;

    // Constructeur par défaut sans initialisation
    public MappingHandler() {
    }

    // Constructeur qui enregistre la classe contrôleur et la méthode à appeler
    public MappingHandler(Class<?> classe, Method methode) {
        this.classe = classe;
        this.methode = methode;
    }

    // Constructeur qui enregistre la classe, la méthode et le pattern d'URL
    public MappingHandler(Class<?> classe, Method methode, UrlPattern urlPattern) {
        this.classe = classe;
        this.methode = methode;
        this.urlPattern = urlPattern;
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

    // Retourne le pattern d'URL associé à ce mapping (pour les URLs avec paramètres)
    public UrlPattern getUrlPattern() {
        return urlPattern;
    }

    // Définit le pattern d'URL associé à ce mapping
    public void setUrlPattern(UrlPattern urlPattern) {
        this.urlPattern = urlPattern;
    }
}
