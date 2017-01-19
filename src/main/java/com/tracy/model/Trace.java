package com.tracy.model;

import lombok.Data;

/**
 * Created by lurenjie on 2017/1/18
 */
@Data
public class Trace {
    private String projectName;
    private String traceId;
    private Integer parentId;
    private Integer spanId;
}
