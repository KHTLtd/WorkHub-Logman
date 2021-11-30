package spring.config.web;

import com.workhub.logman.web.servlet.InfoServlet;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;
import spring.config.app.SpringAppConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

public class LogmanWebApp extends AbstractDispatcherServletInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

        final Dynamic infoServlet = servletContext.addServlet("info", InfoServlet.class);
        infoServlet.addMapping("/servlet/info");
    }

    @Override
    protected WebApplicationContext createServletApplicationContext() {
        final AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(MvcConfig.class);
        return ctx;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/*"};
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        final AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(SpringAppConfig.class);
        return ctx;
    }
}
