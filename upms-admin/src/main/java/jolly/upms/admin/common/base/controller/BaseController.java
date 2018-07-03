package jolly.upms.admin.common.base.controller;

import com.jolly.upms.manager.util.Constant;
import com.jolly.upms.manager.vo.BaseResponseVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator
 * on 2015/7/8.
 */
public class BaseController {

    protected static Logger logger = LogManager.getLogger(BaseController.class);

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    /**
     * 未处理的异常，统一处理
     *
     * @param e 异常信息
     * @return json
     */
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public BaseResponseVO executeException(Exception e) {
        logger.error("系统异常:" + e.getMessage(), e);

        BaseResponseVO vo = new BaseResponseVO();
        vo.setResult(Constant.STATUS_FAIL);
        vo.setMessage("system error");
        return vo;
    }

    @InitBinder
    protected void initBinder(
            WebDataBinder binder) throws ServletException {

    }
}
