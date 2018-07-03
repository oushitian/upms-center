package jolly.upms.admin.web.controller.log;

import com.jolly.upms.manager.annotation.SysLog;
import com.jolly.upms.manager.model.Log;
import com.jolly.upms.manager.model.User;
import com.jolly.upms.manager.service.LogService;
import com.jolly.upms.manager.service.UserService;
import com.jolly.upms.manager.util.DateUtils;
import com.jolly.upms.manager.util.StringUtils;
import com.jolly.upms.manager.vo.Pagination;
import jolly.upms.admin.web.vo.LogQueryVO;
import jolly.upms.admin.web.vo.LogVO;
import jolly.upms.admin.web.vo.PageRespResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author berkeley
 * @since 2017-03-16
 */
@Controller
@RequestMapping("/log")
public class LogController {

    @Resource
    private LogService logService;

    @Resource
    private UserService userService;

    @RequestMapping("/list")
    public String list(Model model) {
        Map<String, String> map = new LinkedHashMap<>();
        SysLog.Operate[] operates = SysLog.Operate.values();
        for (SysLog.Operate operate : operates) {
            map.put(operate.getCode(), operate.getName());
        }
        model.addAttribute("operates", map);
        return "log/logList";
    }

    @RequestMapping("/listData")
    @ResponseBody
    public PageRespResult listData(LogQueryVO logQueryParam) {
        Map params = new HashMap();
        if(StringUtils.isNotBlank(logQueryParam.getUserName())){
            User userQuery = userService.queryByUserName(logQueryParam.getUserName());
            if(userQuery!=null){
                params.put("userId", userQuery.getUserId());
            }
        }
        params.put("operName", logQueryParam.getOperName());
        String startTime = logQueryParam.getStartTime();
        String endTime = logQueryParam.getEndTime();
        if (StringUtils.isNotBlank(startTime)) {
            Integer startTimeInt = DateUtils.getSecondFromDateString(startTime, "yyyy-MM-dd HH:mm");
            params.put("startTime", startTimeInt);
        }
        if (StringUtils.isNotBlank(endTime)) {
            Integer endTimeInt = DateUtils.getSecondFromDateString(endTime, "yyyy-MM-dd HH:mm");
            params.put("endTime", endTimeInt);
        }
        Pagination<Log> pagination = logService.queryList(params, logQueryParam.getStart(), logQueryParam.getRows());
        List<String> userIdsList=new ArrayList<>();
        for (Log log : pagination.getResult()) {
            String userId=log.getUserId();
            userIdsList.add(userId);
        }
        List<Log> appList = pagination.getResult();
        List<LogVO> logVOList = new ArrayList<>();
        Map<Integer, User> userMapByUserIds = userService.getUserMapByUserIds(userIdsList);
        SysLog.Operate[] operates = SysLog.Operate.values();
        for (Log log : appList) {
            LogVO logVO = new LogVO();
            BeanUtils.copyProperties(log, logVO);
            User user = userMapByUserIds.get(Integer.valueOf(log.getUserId()));
            logVO.setUserId(user!=null?user.getUserName():"");
            //老的日志用名称，新的日志用编号，日志表设计不是很合理
            String operName = logVO.getOperName();
            for (SysLog.Operate operate : operates) {
                if (operate.getCode().equals(operName)){
                    logVO.setOperName(operate.getName());
                    break;
                }
            }
            logVOList.add(logVO);
        }
        return new PageRespResult(logVOList, pagination.getTotalCount());
    }


}
