package com.framework;

import java.io.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class FrontServlet extends HttpServlet {
    private static final List<String> INDEX_FILES = Arrays.asList(
        "/index.html", "/index.htm", "/index.jsp"
    );

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Racine : chercher index
        if ("/".equals(path) || path.isEmpty()) {
            String index = findIndex();
            if (index != null) {
                serveResource(index, res);
                return;
            }
            showError(res, "Aucune page d'accueil trouvée");
            return;
        }

        // Bloquer WEB-INF
        if (path.startsWith("/WEB-INF/")) {
            res.sendError(403, "Accès interdit");
            return;
        }
        
        // Servir la ressource si elle existe
        if (getServletContext().getResourceAsStream(path) != null) {
            serveResource(path, res);
            return;
        }
        
        // Non trouvé
        showError(res, "Page non trouvee: " + path);
    }

    private String findIndex() {
        for (String index : INDEX_FILES) {
            if (getServletContext().getResourceAsStream(index) != null) {
                return index;
            }
        }
        return null;
    }
    
    private void serveResource(String path, HttpServletResponse res) throws IOException {
        ServletContext ctx = getServletContext();
        try (InputStream is = ctx.getResourceAsStream(path)) {
            if (is == null) {
                showError(res, "Resource not found: " + path);
                return;
            }
            
            // Déterminer le type de contenu
            String contentType = ctx.getMimeType(path);
            if (contentType != null) {
                res.setContentType(contentType);
            }
            
            // Copier les données
            OutputStream os = res.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
        }
    }

    private void showError(HttpServletResponse res, String message) throws IOException {
        res.setContentType("text/html;charset=UTF-8");
        res.setStatus(404);
        PrintWriter out = res.getWriter();
        out.println("<html><body style='font-family:Arial;padding:40px;text-align:center'>");
        out.println("<h1>Erreur</h1><p>" + message + "</p>");
        out.println("</body></html>");
        out.close();
    }
}