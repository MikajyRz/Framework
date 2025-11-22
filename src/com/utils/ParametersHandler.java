package com.utils;

// Utilitaire qui convertit une valeur String provenant de la requête
// vers le type Java attendu par le paramètre de la méthode (int, double, ...)
public class ParametersHandler {

    // Convertit la chaîne "value" vers le type "targetType" (primitifs + String)
    public static Object convertToType(String value, Class<?> targetType) {
        if (value == null) {
            return getDefaultValue(targetType);
        }

        if (targetType.equals(String.class)) {
            return value;
        } else if (targetType.equals(int.class) || targetType.equals(Integer.class)) {
            return Integer.parseInt(value);
        } else if (targetType.equals(long.class) || targetType.equals(Long.class)) {
            return Long.parseLong(value);
        } else if (targetType.equals(double.class) || targetType.equals(Double.class)) {
            return Double.parseDouble(value);
        } else if (targetType.equals(float.class) || targetType.equals(Float.class)) {
            return Float.parseFloat(value);
        } else if (targetType.equals(boolean.class) || targetType.equals(Boolean.class)) {
            return Boolean.parseBoolean(value);
        }

        // Par défaut, on renvoie la valeur brute (String) si aucun type n'est géré explicitement
        return value;
    }

    // Valeurs par défaut pour les types primitifs quand la valeur est absente (null)
    private static Object getDefaultValue(Class<?> targetType) {
        if (targetType.equals(int.class)) return 0;
        if (targetType.equals(long.class)) return 0L;
        if (targetType.equals(double.class)) return 0.0d;
        if (targetType.equals(float.class)) return 0.0f;
        if (targetType.equals(boolean.class)) return false;
        return null;
    }
}
