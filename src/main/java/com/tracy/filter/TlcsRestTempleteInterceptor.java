package com.tracy.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Created by lurenjie on 2017/1/19
 */
@Slf4j
public class TlcsRestTempleteInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String traceName = MDC.get("tlcs-trace-name");
        String spanId = MDC.get("tlcs-trace-span");
        HttpHeaders headers = request.getHeaders();
        headers.add("tlcs-trace", MDC.get("tlcs-trace"));
        long start = System.currentTimeMillis();
        ClientHttpResponse response = execution.execute(request, body);
        long end = System.currentTimeMillis();
        if (!StringUtils.isEmpty(traceName) && !StringUtils.isEmpty(spanId)) {
            log.info("TLCS-TRACE-RECORD:trace:{} spanId:{} call for next spend:{}", traceName, spanId, end - start);
        }
        return response;
    }
}
