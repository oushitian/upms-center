package com.jolly.upms.facade.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @author chenjc
 * @since 2017-05-25
 */
public class BatchDataPermissionRespDTO implements Serializable {

    private static final long serialVersionUID = -3665957688190430147L;

    /**
     * url
     */
    private String url;

    /**
     * 数据权限集合
     * key存放数据维度属性名
     * value存放数据维度属性值集合
     */
    private Map<String, Set<String>> dataDimensionMap;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Set<String>> getDataDimensionMap() {
        return dataDimensionMap;
    }

    public void setDataDimensionMap(Map<String, Set<String>> dataDimensionMap) {
        this.dataDimensionMap = dataDimensionMap;
    }
}
