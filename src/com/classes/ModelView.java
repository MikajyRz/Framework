package com.classes;

import java.util.HashMap;
import java.util.Map;

public class ModelView {
    private String view;
    private Map<String, Object> data = new HashMap<String, Object>();

    // Retourne le chemin de la vue (JSP ou autre ressource) à afficher
    public String getView() {
        return view;
    }

    // Définit le chemin de la vue (JSP ou autre ressource) à afficher
    public void setView(String view) {
        this.view = view;
    }

    // Retourne la map des données à transmettre à la vue
    public Map<String, Object> getData() {
        return data;
    }

    // Remplace la map des données à transmettre à la vue
    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    // Ajoute une donnée (clé/valeur) au modèle à envoyer vers la vue
    public void addData(String key, Object value) {
        if (this.data != null) {
            this.data.put(key, value);
        }
    }
}
