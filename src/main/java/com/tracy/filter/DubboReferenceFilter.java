package com.tracy.filter;


import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;

/**
 * Dubbo Filter扩展，调用方使用。
 * <p>
 * 从MDC中取出当前线程的REQID，加入invocation的attachments
 * Created by lurenjie on 2017/4/25
 */
@Slf4j
@Activate(group = Constants.REFERENCE_FILTER_KEY, order = 1)
public class DubboReferenceFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String hashedReqId = MDC.get("REQID");
        if (StringUtils.isEmpty(hashedReqId)) {
            hashedReqId = generateReqId();
        }
        invocation.getAttachments().put("REQID", hashedReqId);
        Result result = invoker.invoke(invocation);
        log.info(JSON.toJSONString(result));
        return result;
    }

    private String generateReqId() {
        return RandomStringUtils.randomAlphanumeric(8);
    }
}
