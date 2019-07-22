package cn.hnust.bookshop.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import cn.hnust.bookshop.bean.ResponseResult;
import cn.hnust.bookshop.util.JwtTokenHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpHeaders.ORIGIN;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter extends OncePerRequestFilter {
    private AntPathMatcher matcher = new AntPathMatcher();
    private static final String pattern = "/admin/**";
    private static final List<String> excludeList = new ArrayList<String>(Arrays.asList("/admin/login","/admin/verify"));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String origin = request.getHeader(ORIGIN);
        response.setHeader("Access-Control-Allow-Origin", "*");//* or origin as u prefer
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "PUT, POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "content-type, authorization,token");

        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            if (!excludeList.contains(request.getRequestURI()) && matcher.match(pattern, request.getRequestURI())) {
                ResponseResult result = JwtTokenHelper.checkToken(request);
                if (result != null) {
                    response.setContentType("application/json; charset=utf-8");
                    response.setCharacterEncoding("UTF-8");
                    ObjectMapper mapper = new ObjectMapper();
                    PrintWriter writer = response.getWriter();
                    writer.write(mapper.writeValueAsString(result));
                    writer.close();
                } else {
                    filterChain.doFilter(request, response);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
