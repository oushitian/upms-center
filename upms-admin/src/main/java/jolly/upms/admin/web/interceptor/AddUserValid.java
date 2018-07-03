package jolly.upms.admin.web.interceptor;

import jolly.upms.admin.common.base.interceptor.SignValid;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author chenjc
 * @since 2018-02-05
 */
public class AddUserValid extends SignValid {

    AddUserValid() {
        List<String> paramsKey = Arrays.asList(
                "charset",
                "ip",
                "appKey",
                "signType",
                "key"
        );
        Collections.sort(paramsKey);
        setParamsKey(paramsKey);
    }
}
