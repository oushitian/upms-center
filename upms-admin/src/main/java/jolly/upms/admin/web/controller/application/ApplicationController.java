package jolly.upms.admin.web.controller.application;

import com.jolly.upms.manager.annotation.SysLog;
import com.jolly.upms.manager.model.Application;
import com.jolly.upms.manager.model.AuthUser;
import com.jolly.upms.manager.model.User;
import com.jolly.upms.manager.service.ApplicationService;
import com.jolly.upms.manager.service.AuthService;
import com.jolly.upms.manager.service.MenuService;
import com.jolly.upms.manager.service.UserService;
import com.jolly.upms.manager.util.Assert;
import com.jolly.upms.manager.util.DateUtils;
import com.jolly.upms.manager.util.OriginalObjectHolder;
import com.jolly.upms.manager.vo.Pagination;
import jolly.upms.admin.web.vo.AppQueryVO;
import jolly.upms.admin.web.vo.AppVO;
import jolly.upms.admin.web.vo.PageRespResult;
import jolly.upms.admin.web.vo.RespResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

/**
 * @author berkeley
 * @since 2017-03-16
 */
@Controller
@RequestMapping("/app")
public class ApplicationController {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Resource
    private ApplicationService applicationService;
    @Resource
    private AuthService authService;
    @Resource
    private UserService userService;

    @Resource
    private MenuService menuService;

    @RequestMapping("/list")
    public String list(Model model, HttpServletRequest request) {
        Set<String> permissionStrings= (Set<String>) authService.getFunctionPermission(request).getData();
        model.addAttribute("permissionStrings", permissionStrings);
        return "app/appList";
    }

    @RequestMapping("/listData")
    @ResponseBody
    public PageRespResult listData(AppQueryVO appParam, HttpServletRequest request) {
        List<AppVO> appVOList = new ArrayList<>();
        Set<Integer> permitApplicationIds = authService.getRolePermitApplicationIds(request);
        if (permitApplicationIds.isEmpty()) {
            return new PageRespResult(appVOList, 0);
        }
        Map params = new HashMap();
        params.put("appName", appParam.getAppName());
        params.put("permitApplicationIds", permitApplicationIds);
        Pagination<Application> pagination = applicationService.queryList(params, appParam.getStart(), appParam.getRows());
        List<Application> appList = pagination.getResult();
        List<Integer> userIdsList=new ArrayList<>();
        for (Application application : appList) {
            if(StringUtils.isNotBlank(application.getModifier())){
                Integer userId=Integer.valueOf(application.getModifier());
                userIdsList.add(userId);
            }
        }
        Map<Integer, User> userMapByUserIds = userService.getUserMapByUserIds(userIdsList);
        for (Application application : appList) {
            AppVO appVO = new AppVO();
            BeanUtils.copyProperties(application, appVO);
            if(StringUtils.isNotBlank(application.getModifier())){
                User user = userMapByUserIds.get(Integer.valueOf(application.getModifier()));
                appVO.setModifier(user!=null?user.getUserName():"");
            }
            appVOList.add(appVO);
        }
        return new PageRespResult(appVOList, pagination.getTotalCount());
    }

    @RequestMapping("/save")
    @ResponseBody
    @SysLog(SysLog.Operate.APP_INSERT)
    public RespResult save(@Valid AppVO appVO, BindingResult bingResult, HttpServletRequest request) {
        if (bingResult.hasErrors()) {
            return new RespResult(RespResult.RespCode.PARAM_EXCEPTION.getCode(), bingResult.getAllErrors().get(0).getDefaultMessage());
        }
        AuthUser authUser = authService.getAuthUser(request);
        Assert.notNull(authUser,RespResult.RespCode.NO_LOGIN.getMsg());
        Application application = new Application();
        application.setThisAppName(appVO.getAppName());
        if (applicationService.selectDuplicateCount(application) > 0) {
            return new RespResult(RespResult.RespCode.APP_NAME_DUPLICATE.getCode(), RespResult.RespCode.APP_NAME_DUPLICATE.getMsg());
        }
        application.setThisAppName("");
        application.setThisAppKey(appVO.getAppKey());
        if (applicationService.selectDuplicateCount(application) > 0) {
            return new RespResult(RespResult.RespCode.APP_KEY_DUPLICATE.getCode(), RespResult.RespCode.APP_KEY_DUPLICATE.getMsg());
        }
        application.setThisAppKey(appVO.getAppKey()).setThisAppName(appVO.getAppName()).setThisDescription(appVO.getDescription()).setThisGmtCreated(DateUtils.getCurrentSecondIntValue()).setThisGmtModified(DateUtils.getCurrentSecondIntValue()).setModifier(String.valueOf(authUser.getUserId()));
        if (StringUtils.isBlank(appVO.getDescription())) {
            application.setDescription(appVO.getAppName());
        }
        application.setDomainName(appVO.getDomainName());
        applicationService.save(application);
        return new RespResult();
    }


    @RequestMapping("/doModify")
    @ResponseBody
    @SysLog(SysLog.Operate.APP_UPDATE)
    public RespResult doModify(@Valid AppVO appVO, BindingResult bingResult, HttpServletRequest request, HttpServletResponse response) {
        if (bingResult.hasErrors()) {
            return new RespResult(RespResult.RespCode.PARAM_EXCEPTION.getCode(), bingResult.getAllErrors().get(0).getDefaultMessage());
        }
        AuthUser authUser = authService.getAuthUser(request);
        if (authUser == null) {
            return new RespResult(RespResult.RespCode.NO_LOGIN.getCode(), RespResult.RespCode.NO_LOGIN.getMsg());
        }
        Application applicationDO = applicationService.get(appVO.getApplicationId());
        if (applicationDO == null) {
            return new RespResult(RespResult.RespCode.NO_ENTITY.getCode(), RespResult.RespCode.NO_ENTITY.getMsg());
        }
        Application originalObject = new Application();
        BeanUtils.copyProperties(applicationDO, originalObject);
        OriginalObjectHolder.putOriginal(originalObject);
        if (applicationDO == null) {
            return new RespResult(RespResult.RespCode.NO_ENTITY.getCode(), RespResult.RespCode.NO_ENTITY.getMsg());
        }
        Application application = new Application();
        application.setApplicationId(appVO.getApplicationId());
        application.setThisAppName(appVO.getAppName());
        if (applicationService.selectEditDuplicateCount(application) > 0) {
            return new RespResult(RespResult.RespCode.APP_NAME_DUPLICATE.getCode(), RespResult.RespCode.APP_NAME_DUPLICATE.getMsg());
        }
        application.setThisAppName("");
        application.setThisAppKey(appVO.getAppKey());
        if (applicationService.selectEditDuplicateCount(application) > 0) {
            return new RespResult(RespResult.RespCode.APP_KEY_DUPLICATE.getCode(), RespResult.RespCode.APP_KEY_DUPLICATE.getMsg());
        }

        applicationDO.setThisAppName(appVO.getAppName()).setThisGmtModified(DateUtils.getCurrentSecondIntValue()).setModifier(String.valueOf(authUser.getUserId()));
        if (StringUtils.isNotBlank(appVO.getDescription())) {
            applicationDO.setDescription(appVO.getDescription());
        }
        applicationDO.setDomainName(appVO.getDomainName());
        applicationDO.setAppKey(appVO.getAppKey());
        applicationService.updateSelective(applicationDO);
        OriginalObjectHolder.putNew(applicationDO);

        return new RespResult();
    }


    @RequestMapping("/delete")
    @ResponseBody
    @SysLog(SysLog.Operate.APP_DELETE)
    public RespResult delete(@RequestParam(value = "applicationId",required = true) Integer applicationId, HttpServletRequest request) {
        AuthUser authUser = authService.getAuthUser(request);
        Assert.notNull(authUser,RespResult.RespCode.NO_LOGIN.getMsg());
        Application applicationDO = applicationService.get(applicationId);
        Assert.notNull(applicationDO,RespResult.RespCode.NO_ENTITY.getMsg());
        Assert.isTrue(menuService.countByApplicationId(applicationId)==0,"应用已经在使用中，不能被删除");
        applicationDO.setModifier(String.valueOf(authUser.getUserId()));
        applicationDO.setGmtModified(DateUtils.getCurrentSecondIntValue());
        applicationDO.setIsDeleted(1);
        applicationService.updateSelective(applicationDO);
        OriginalObjectHolder.putOriginal(applicationDO);
        return new RespResult();
    }



}
