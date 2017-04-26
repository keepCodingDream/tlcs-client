package com.tracy.filter;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.tracy.model.Trace;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by lurenjie on 2017/1/18
 */
public class TlcsFilter implements Filter {
    @Value("${tlcs.project.name}")
    private String projectName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        MDC.clear();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String trace = request.getHeader("tlcs-trace");
        Trace traceModel;
        if (!StringUtils.isEmpty(trace)) {
            traceModel = JSON.parseObject(trace, Trace.class);
            traceModel.setParentId(traceModel.getSpanId());
            traceModel.setSpanId(traceModel.getParentId() + 1);
            traceModel.setProjectName(projectName);
        } else {
            traceModel = new Trace();
            traceModel.setTraceId(UUID.randomUUID().toString());
            traceModel.setParentId(-1);
            traceModel.setSpanId(1);
            traceModel.setProjectName(projectName);
            response.addHeader("tlcs-trace", JSON.toJSONString(traceModel));
        }
        MDC.put("tlcs-trace", JSON.toJSONString(traceModel));
        MDC.put("tlcs-trace-name", traceModel.getTraceId());
        MDC.put("tlcs-trace-span", traceModel.getSpanId().toString());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
