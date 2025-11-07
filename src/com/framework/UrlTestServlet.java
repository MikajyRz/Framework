package com.framework;

import com.annotations.ControllerAnnotation;
import com.annotations.HandleUrl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class UrlTestServlet extends HttpServlet {
    private static class MappingInfo {
        final Class<?> controllerClass;
        final Method method;
        MappingInfo(Class<?> controllerClass, Method method) {
            this.controllerClass = controllerClass;
            this.method = method;
        }
    }

    private final Map<String, MappingInfo> mappings = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Optionally allow a base package via init-param, default to test.java
        String basePackage = Optional.ofNullable(config.getInitParameter("basePackage"))
                .filter(s -> !s.isBlank())
                .orElse("test.java");
        try {
            List<Class<?>> classes = getClasses(basePackage);
            for (Class<?> clazz : classes) {
                if (!clazz.isAnnotationPresent(ControllerAnnotation.class)) continue;
                for (Method m : clazz.getDeclaredMethods()) {
                    if (!m.isAnnotationPresent(HandleUrl.class)) continue;
                    HandleUrl hu = m.getAnnotation(HandleUrl.class);
                    String v = hu.value();
                    if (v == null || v.isEmpty()) v = m.getName();
                    String key = normalizePath(v);
                    mappings.put(key, new MappingInfo(clazz, m));
                }
            }
            // Expose mappings to the application so FrontServlet can detect and forward
            getServletContext().setAttribute("URLTEST_MAPPINGS", mappings);
        } catch (Exception e) {
            throw new ServletException("Failed to initialize UrlTestServlet mappings", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null) {
            String uri = req.getRequestURI();
            String ctx = req.getContextPath();
            path = uri.startsWith(ctx) ? uri.substring(ctx.length()) : uri;
        }
        String key = normalizePath(path == null ? "" : path);
        MappingInfo info = mappings.get(key);

        // Fallback to FrontServlet if no mapping found
        if (info == null) {
            try {
                RequestDispatcher rd = getServletContext().getNamedDispatcher("FrontServlet");
                if (rd != null) {
                    rd.forward(req, resp);
                    return;
                }
            } catch (Exception e) {
                // If forwarding fails, we let FrontServlet handle default 404 via direct call
            }
            return;
        }

        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><body style='font-family:Arial;padding:24px'>");
            out.println("<h2>UrlTestServlet</h2>");
            out.println("<div>Requete: <code>" + escapeHtml(req.getRequestURI()) + "</code></div>");
            out.println("<div>Path recherche: <code>" + escapeHtml(key) + "</code></div><hr/>");
            out.println("<div>classe: " + escapeHtml(info.controllerClass.getSimpleName() + ".java") + "</div>");
            out.println("<div>methode: " + escapeHtml(info.method.getName()) + "</div>");
            out.println("</body></html>");
        }
    }

    public static String normalizePath(String p) {
        if (p == null) return "";
        String s = p.trim();
        if (!s.startsWith("/")) s = "/" + s;
        // Remove trailing slash except root
        if (s.length() > 1 && s.endsWith("/")) s = s.substring(0, s.length() - 1);
        return s;
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    // --- Classpath scanning (file protocol) ---
    private static List<Class<?>> getClasses(String packageName) throws IOException, ClassNotFoundException {
        String path = packageName.replace('.', '/');
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL resource = cl.getResource(path);
        if (resource == null) return Collections.emptyList();
        if (!"file".equals(resource.getProtocol())) return Collections.emptyList();
        File dir = new File(resource.getFile());
        return findClasses(dir, packageName);
    }

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
}
