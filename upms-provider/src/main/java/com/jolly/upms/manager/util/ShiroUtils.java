package com.jolly.upms.manager.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zuoqiuming on 2017/1/17 0017.
 */
public class ShiroUtils {

    //资源访问路径转换成权限串,如果路径最后带了数值的参数,则去除,不作为权限串的部分
    //aaa/bbb=>aaa:bbb
    public static String convertPathToPermissionString(String path) {
        return convertToPermissionString(path, true);
    }

    /**
     * 转换原始字符串为权限串的格式
     *
     * @param source
     * @param filterDigits:是否过滤最后的数值
     * @return
     */
    public static String convertToPermissionString(String source, boolean filterDigits) {
        if (source == null) {
            return source;
        }
        //去除参数,且去除前后的'/'
        String[] pathElement = source.replaceAll("\\?.*", "").replaceAll("(^/)|(/$)", "").split("/");
        List<String> pathElementList = new ArrayList<>();
        for (int i = 0; i < pathElement.length; i++) {
            String element = pathElement[i];
            //必要时,最后一个字段为数值的,需要剔除
            if (!filterDigits || i < pathElement.length - 1) {
                if (StringUtils.isNotBlank(element)) {
                    pathElementList.add(element);
                }
            } else if (!NumberUtils.isDigits(element)) {
                pathElementList.add(element);
            }
        }
        String permStr = StringUtils.join(pathElementList, ":");
        if (StringUtils.isNotBlank(permStr)) {
            if (permStr.contains("jsessionid")) {
                permStr = StringUtils.substringAfter(permStr, ":");
            }
        }
        return permStr;
    }

    /**
     * requestMapping==>aaa:bbb,过滤{param}
     *
     * @param requestMapping
     * @return
     */
    public static String convertRequestMappingToPermissionString(String requestMapping) {
        if (requestMapping == null) {
            return requestMapping;
        }
        //剔除表达式类型,例如{orderId},不作为权限的控制部分
        requestMapping = requestMapping.replaceAll("\\{.*\\}/*", "");
        return convertToPermissionString(requestMapping, false);
    }

}
