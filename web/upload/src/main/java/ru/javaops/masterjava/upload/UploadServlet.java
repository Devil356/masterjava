package ru.javaops.masterjava.upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import ru.javaops.ThymeleafUtil;
import ru.javaops.masterjava.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@WebServlet("/")
public class UploadServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(UploadServlet.class);
    private final UserUpload userUpload = new UserUpload();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        LOG.info("doGet()...");
        System.out.println("doGet()...");
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        final TemplateEngine engine = ThymeleafUtil.getTemplateEngine(getServletContext());
        engine.process("upload", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        LOG.info("doPost()...");
        System.out.println("doPost()...");
        final ServletFileUpload upload = new ServletFileUpload();
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        final TemplateEngine engine = ThymeleafUtil.getTemplateEngine(getServletContext());

        try {
            final FileItemIterator itemIterator = upload.getItemIterator(req);
            while (itemIterator.hasNext()) {
                FileItemStream fileItemStream = itemIterator.next();
                if (!fileItemStream.isFormField()) {
                    try(InputStream is = fileItemStream.openStream()) {
                        List<User> users = userUpload.process(is);
                        users.forEach(u -> LOG.info(u.toString()));
                        webContext.setVariable("users", users);
                        engine.process("result", webContext, resp.getWriter());
                    }
                    break;
                }
            }
            LOG.info("XML successfully uploaded");
        } catch (Exception e) {
            webContext.setVariable("exception", e);
            engine.process("exception", webContext, resp.getWriter());
        }
        resp.sendRedirect(req.getContextPath());
    }
}
