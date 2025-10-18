package main.java.framework;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

public class FrontServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI();
        String contextPath = req.getContextPath();
        if (contextPath != null && !contextPath.isEmpty() && path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }

        if (path != null && !path.isEmpty()) {
            ServletContext context = getServletContext();
            URL resourceUrl = null;
            try {
                resourceUrl = context.getResource(path);
            } catch (MalformedURLException ignored) {
            }

            if (resourceUrl != null) {
                RequestDispatcher defaultDispatcher = context.getNamedDispatcher("default");
                if (defaultDispatcher != null) {
                    defaultDispatcher.forward(req, resp);
                    return;
                }
                RequestDispatcher rd = context.getRequestDispatcher(path);
                if (rd != null) {
                    rd.forward(req, resp);
                    return;
                }
            }
        }

        String fullUrl = req.getRequestURL().toString();
        String url = req.getRequestURI();  

        System.out.println("URL complet: "+ fullUrl);
        System.out.println("Url: " +url);

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>Recuperation URL :</h1>");
        out.println("<p>Full URL : " + fullUrl + "</p>");
        out.println("<p>URL : " + url + "</p>");
        out.println("</body></html>");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}