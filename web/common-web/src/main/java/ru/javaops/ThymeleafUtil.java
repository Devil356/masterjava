package ru.javaops;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;

public class ThymeleafUtil {

    private ThymeleafUtil(){

    }
    public static TemplateEngine getTemplateEngine(ServletContext context) {
        final ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(context);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        //темплейты будут лежать тут
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        //через какой период будет обновляться кэш, а следовательно и страница Thymeleaf
        templateResolver.setCacheTTLMs(1000L);
        //движок который обрабатывает наши темплейты. То есть, если в наших шаблонах есть какая-то логика, то таймлив
        //процессит шаблоны и заменяет переменные реальными значениями, подставляет фрагменты, и на выходе получаем
        //готовый html. Замена JSP. По-умолчанию, в спрингбуте нет JSP, а есть Thymeleaf
        final TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(templateResolver);
        return engine;
    }
}
