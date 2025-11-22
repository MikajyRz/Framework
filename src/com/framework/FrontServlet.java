package com.framework;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import com.utils.*;
import com.classes.ModelView;

public class FrontServlet extends HttpServlet {
    private RequestDispatcher defaultDispatcher;
    private static final List<String> INDEX_FILES = Arrays.asList(
        "/index.html", "/index.htm", "/index.jsp"
    );

    // Initialisation du servlet frontal : scan des contrôleurs et enregistrement des mappings
    @Override
    public void init() throws ServletException {
        defaultDispatcher = getServletContext().getNamedDispatcher("default");
        try {
            // Adapter ici le package des contrôleurs à ton projet
            Map<String, MappingHandler> urlMappings = ScanningUrl.scanUrlMappings("test.java");
            getServletContext().setAttribute("urlMappings", urlMappings);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // Méthode centrale qui intercepte toutes les requêtes HTTP et décide du traitement
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String path = req.getRequestURI().substring(req.getContextPath().length());

        @SuppressWarnings("unchecked")
        Map<String, MappingHandler> urlMappings = (Map<String, MappingHandler>) getServletContext().getAttribute("urlMappings");

        if (path.equals("/") || path.isEmpty()) {
            String indexPath = findExistingIndex();
            if (indexPath != null) {
                req.getRequestDispatcher(indexPath).forward(req, res);
                return;
            } else {
                customServe(req, res);
                return;
            }
        }

        MappingHandler mapH = urlMappings != null ? urlMappings.get(path) : null;

        boolean resourceExists = getServletContext().getResource(path) != null;
        if (resourceExists) {
            defaultServe(req, res);
        } else if (mapH != null) {
            try {
                handleMapping(req, res, mapH);
            } catch (Exception e) {
                throw new ServletException(e);
            }
        } else {
            customServe(req, res);
        }
    }

    // Exécute la méthode de contrôleur associée à l'URL et gère son type de retour
    private void handleMapping(HttpServletRequest req, HttpServletResponse res, MappingHandler mapH)
        throws Exception {
        Class<?> returnType = mapH.getMethode().getReturnType();
        Object instance = mapH.getClasse().getDeclaredConstructor().newInstance();

        if (returnType.equals(String.class)) {
            Object result = mapH.getMethode().invoke(instance);
            res.setContentType("text/plain;charset=UTF-8");
            res.getWriter().println((String) result);
        } else if (returnType.equals(ModelView.class)) {
            Object mvM = mapH.getMethode().invoke(instance);
            ModelView mv = (ModelView) mvM;
            if (mv.getData() != null) {
                for (Map.Entry<String, Object> entry : mv.getData().entrySet()) {
                    req.setAttribute(entry.getKey(), entry.getValue());
                }
            }
            req.getRequestDispatcher(mv.getView()).forward(req, res);
        } else {
            res.setContentType("text/plain;charset=UTF-8");
            res.getWriter().println("Le type de retour n'est pas un String ni un ModelView.");
        }
    }

    // Recherche un fichier index par défaut (index.html / index.jsp, ...)
    private String findExistingIndex() {
        for (String index : INDEX_FILES) {
            try {
                if (getServletContext().getResource(index) != null) {
                    return index;
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid path in INDEX_FILES: " + index, e);
            }
        }
        return null;
    }

    // Réponse HTML personnalisée lorsqu'aucune ressource ni mapping n'est trouvée
    private void customServe(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = res.getWriter()) {
            String uri = req.getRequestURI();
            String responseBody = """
                <html>
                    <head><title>Resource Not Found</title></head>
                    <body style=\"font-family:sans-serif;\">
                        <p>Aucun fichier ni url correspondant n'a ete trouve.</p>
                        <p>URL demandée : <strong>%s</strong></p>
                    </body>
                </html>
                """.formatted(uri);
            out.println(responseBody);
        }
    }

    // Délègue le traitement au servlet par défaut du conteneur (fichiers statiques, JSP...)
    private void defaultServe(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        if (defaultDispatcher != null) {
            defaultDispatcher.forward(req, res);
        } else {
            customServe(req, res);
        }
    }
}