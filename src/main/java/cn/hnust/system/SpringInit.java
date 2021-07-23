package cn.hnust.system;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang3.StringUtils;

public class SpringInit implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        try {
            String contextPath = context.getContextPath();
            String baseUrl = "";
            if (StringUtils.isNotEmpty(contextPath)) {
                baseUrl += contextPath;
                context.setAttribute("baseUrl", baseUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
