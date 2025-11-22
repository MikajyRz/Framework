package com.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.annotations.ControllerAnnotation;
import com.annotations.HandleUrl;

public class ScanningUrl {
    // Charge dynamiquement toutes les classes d'un package donné
    public static List<Class<?>> getClasses(String packageName) throws IOException, ClassNotFoundException {
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) return new ArrayList<>();

        File directory = new File(resource.getFile());
        return findClasses(directory, packageName);
    }

    // Parcourt récursivement un répertoire pour trouver toutes les classes d'un package
    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) return classes;

        File[] files = directory.listFiles();
        if (files == null) return classes;

        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().replaceAll("\\.class$", "");
                classes.add(Class.forName(className));
            }
        }
        return classes;
    }

    // Scanne les contrôleurs annotés et construit la map URL -> MappingHandler
    public static Map<String, MappingHandler> scanUrlMappings(String packageName) throws Exception {
        Map<String, MappingHandler> urlMappings = new HashMap<>();
        List<Class<?>> controllers = getClasses(packageName);
        for (Class<?> clazz : controllers) {
            if (clazz.isAnnotationPresent(ControllerAnnotation.class)) {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(HandleUrl.class)) {
                        String url = method.getAnnotation(HandleUrl.class).value();
                        if (url == null) url = "";
                        if (!url.isEmpty() && !url.startsWith("/")) {
                            url = "/" + url;
                        }
                        if (url.isEmpty()) {
                            url = "/";
                        }

                        // Utilise directement la valeur de @HandleUrl comme clé et comme pattern
                        urlMappings.put(url, new MappingHandler(clazz, method, new UrlPattern(url)));
                    }
                }
            }
        }
        return urlMappings;
    }
}
