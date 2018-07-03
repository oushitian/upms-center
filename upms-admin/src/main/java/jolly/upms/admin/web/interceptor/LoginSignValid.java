package jolly.upms.admin.web.interceptor;


import jolly.upms.admin.common.base.interceptor.SignValid;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by dengjunbo
 * on 16/5/19.
 */
public class LoginSignValid extends SignValid {

    LoginSignValid() {
        List<String> paramsKey = Arrays.asList(
                "charset",
                "ip",
                "password",
                "appKey",
                "signType",
                "key"
        );

        Collections.sort(paramsKey);
        setParamsKey(paramsKey);
    }

}
