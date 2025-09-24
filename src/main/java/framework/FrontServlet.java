package main.java.framework;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class FrontServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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