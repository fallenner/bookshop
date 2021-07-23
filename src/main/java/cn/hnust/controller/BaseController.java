package cn.hnust.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import cn.hnust.domain.Msg;


public class BaseController {
    protected Msg msg = new Msg();

    protected int getInt(HttpServletRequest request, String param, int defaultValue) {
        String value = request.getParameter(param);
        return NumberUtils.isNumber(value) ? Integer.parseInt(value) : defaultValue;
    }

    protected Integer getInteger(HttpServletRequest request, String param) {
        String value = request.getParameter(param);
        return NumberUtils.isNumber(value) ? Integer.parseInt(value) : null;
    }

    protected Integer getInteger(HttpServletRequest request, String param, Integer defaultValue) {
        String value = request.getParameter(param);
        return NumberUtils.isNumber(value) ? Integer.parseInt(value) : defaultValue;
    }

    protected String get(HttpServletRequest request, String param) {
        String value = request.getParameter(param);
        return value != null ? value.trim() : null;
    }

    protected String get(HttpServletRequest request, String param, String defaultValue) {
        String value = request.getParameter(param);
        return StringUtils.isNotBlank(value) ? value.trim() : defaultValue;
    }

    protected String[] getValues(HttpServletRequest request, String param) {
        String[] values = request.getParameterValues(param);
        return values == null ? new String[0] : values;
    }

    protected int getPageSize(HttpServletRequest request) {
        String rows = request.getParameter("rows");
        if (StringUtils.isEmpty(rows))
            rows = "10";
        return Integer.parseInt(rows);
    }

    protected int getPageNum(HttpServletRequest request) {
        String page = request.getParameter("page");
        if (StringUtils.isEmpty(page))
            page = "1";
        return Integer.parseInt(page);
    }
}
