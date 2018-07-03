package jolly.upms.admin.web.controller.datadimension;

import com.jolly.upms.manager.annotation.SysLog;
import com.jolly.upms.manager.dao.MenuDao;
import com.jolly.upms.manager.dao.RolePermissionDao;
import com.jolly.upms.manager.model.*;
import com.jolly.upms.manager.service.AuthService;
import com.jolly.upms.manager.service.DataDimensionService;
import com.jolly.upms.manager.service.DataDimensionValuesService;
import com.jolly.upms.manager.service.UserService;
import com.jolly.upms.manager.util.Assert;
import com.jolly.upms.manager.util.DateUtils;
import com.jolly.upms.manager.util.OriginalObjectHolder;
import com.jolly.upms.manager.vo.Pagination;
import jolly.upms.admin.web.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * @author berkeley
 * @since 2017-03-16
 */
@Controller
@RequestMapping("/dataDimension")
public class DataDimensionController   {
    private static final Logger logger = LoggerFactory.getLogger(DataDimensionController.class);

    @Resource
    private DataDimensionService dataDimensionService;

    @Resource
    private DataDimensionValuesService dataDimensionValuesService;

    @Resource
    private MenuDao menuDao;

    @Resource
    private AuthService authService;

    @Resource
    private UserService userService;

    @Resource
    private RolePermissionDao rolePermissionDao;

    @RequestMapping("/list")
    public String list(Model model, HttpServletRequest request) {
        Set<String> permissionStrings= (Set<String>) authService.getFunctionPermission(request).getData();
        model.addAttribute("permissionStrings", permissionStrings);
        return "dataDimension/dataDimensionList";
    }


    @RequestMapping("/listDimensionValues")
    public String listDimensionValues(Model model, HttpServletRequest request) {
        List<DataDimension> dataDimensionList = dataDimensionService.queryList(null);
        model.addAttribute("dataDimensionList", dataDimensionList);
        Set<String> permissionStrings= (Set<String>) authService.getFunctionPermission(request).getData();
        model.addAttribute("permissionStrings", permissionStrings);
        return "dataDimension/dataDimensionValuesList";
    }

    @RequestMapping("/listDimensionValueData")
    @ResponseBody
    public PageRespResult listDimensionValueData(DataDimensionValueQueryVO dataDimensionValueQueryVO) {
        Map params = new HashMap();
        if(StringUtils.isNotBlank( dataDimensionValueQueryVO.getDimensionId())){
            params.put("dimensionId",dataDimensionValueQueryVO.getDimensionId());
        }
        if(StringUtils.isNotBlank( dataDimensionValueQueryVO.getDisplayName())){
            params.put("displayName",dataDimensionValueQueryVO.getDisplayName() );
        }
        Pagination<DataDimensionValues> pagination = dataDimensionValuesService.queryList(params, dataDimensionValueQueryVO.getStart(), dataDimensionValueQueryVO.getRows());
        List<DataDimensionValues> dataDimensionValues = pagination.getResult();
        List<DataDimensionValuesVO> dataDimensionValuesVOs = new ArrayList<>();
        List<DataDimension> dataDimensionList = dataDimensionService.queryList(null);
        Map<Integer, DataDimension> integerDataDimensionMap = changeListTOMap(dataDimensionList);
        List<String> userIdsList=new ArrayList<>();
        for (DataDimensionValues dataDimensionValue : dataDimensionValues) {
            String userId=dataDimensionValue.getModifier();
            userIdsList.add(userId);
        }
        Map<Integer, User> userMapByUserIds = userService.getUserMapByUserIds(userIdsList);
        for (DataDimensionValues dataDimensionValue : dataDimensionValues) {
            DataDimensionValuesVO dataDimensionValuesVO = new DataDimensionValuesVO();
            BeanUtils.copyProperties(dataDimensionValue, dataDimensionValuesVO);
            dataDimensionValuesVOs.add(dataDimensionValuesVO);
            if(dataDimensionValuesVO.getDimensionId()!=null&&integerDataDimensionMap.get(dataDimensionValuesVO.getDimensionId())!=null){
                dataDimensionValuesVO.setDimensionDisplayName(integerDataDimensionMap.get(dataDimensionValuesVO.getDimensionId()).getDisplayName());
            }
            if(StringUtils.isNotBlank(dataDimensionValue.getModifier())){
                User user = userMapByUserIds.get(Integer.valueOf(dataDimensionValue.getModifier()));
                dataDimensionValuesVO.setModifier(user!=null?user.getUserName():"");
            }
        }
        return new PageRespResult(dataDimensionValuesVOs, pagination.getTotalCount());
    }

    private Map<Integer, DataDimension> changeListTOMap(List<DataDimension> lists) {
        Map<Integer, DataDimension> dataDimensionsMap=new HashMap<>();
         if (CollectionUtils.isEmpty(lists)){
            return dataDimensionsMap;
         }
         for(DataDimension dataDimension:lists){
             dataDimensionsMap.put(dataDimension.getDimensionId(),dataDimension);
         }
        return dataDimensionsMap;
    }

    @RequestMapping("/listData")
    @ResponseBody
    public PageRespResult listData(DataDimensionQueryVO dataDimensionQuery) {
        Map params = new HashMap();
        params.put("attributeName", dataDimensionQuery.getAttributeName());
        params.put("displayName", dataDimensionQuery.getDisplayName());
        Pagination<DataDimension> pagination = dataDimensionService.queryList(params, dataDimensionQuery.getStart(), dataDimensionQuery.getRows());
        List<DataDimension> appList = pagination.getResult();
        List<DataDimensionVO> appVOList = new ArrayList<>();
        List<String> userIdsList=new ArrayList<>();
        for (DataDimension dataDimension : appList) {
            String userId=dataDimension.getModifier();
            userIdsList.add(userId);
        }
        Map<Integer, User> userMapByUserIds = userService.getUserMapByUserIds(userIdsList);
        for (DataDimension dataDimension : appList) {
            DataDimensionVO dataDimensionVO = new DataDimensionVO();
            BeanUtils.copyProperties(dataDimension, dataDimensionVO);
            if(StringUtils.isNotBlank(dataDimension.getModifier())){
                User user = userMapByUserIds.get(Integer.valueOf(dataDimension.getModifier()));
                dataDimensionVO.setModifier(user!=null?user.getUserName():"");
            }
            appVOList.add(dataDimensionVO);
        }
        return new PageRespResult(appVOList, pagination.getTotalCount());
    }

    @RequestMapping("/saveDimensionValue")
    @ResponseBody
    @SysLog(SysLog.Operate.DIMENSION_VALUE_INSERT)
    public RespResult saveDimensionValue(@Valid DataDimensionValuesVO dataDimensionValuesVO, BindingResult bingResult, HttpServletRequest request) {
        if (bingResult.hasErrors()) {
            return new RespResult(RespResult.RespCode.PARAM_EXCEPTION.getCode(), bingResult.getAllErrors().get(0).getDefaultMessage());
        }
        AuthUser authUser = authService.getAuthUser(request);
        Assert.notNull(authUser,RespResult.RespCode.NO_LOGIN.getMsg());
        DataDimensionValues dataDimensionValues = new DataDimensionValues();
        dataDimensionValues.setDimensionId_(dataDimensionValuesVO.getDimensionId()).setDisplayName_(dataDimensionValuesVO.getDisplayName()).setAttributeValue_(dataDimensionValuesVO.getAttributeValue());
        dataDimensionValues.setModifier(String.valueOf(authUser.getUserId()));
        dataDimensionValues.setGmtModified(DateUtils.getCurrentSecondIntValue());
        dataDimensionValuesService.save(dataDimensionValues);
        return new RespResult();
    }

    @RequestMapping("/save")
    @ResponseBody
    @SysLog(SysLog.Operate.DIMENSION_NAME_INSERT)
    public RespResult save(@Valid DataDimensionVO dataDimensionVO, BindingResult bingResult, HttpServletRequest request) {
        if (bingResult.hasErrors()) {
            return new RespResult(RespResult.RespCode.PARAM_EXCEPTION.getCode(), bingResult.getAllErrors().get(0).getDefaultMessage());
        }
        AuthUser authUser = authService.getAuthUser(request);
        Assert.notNull(authUser,RespResult.RespCode.NO_LOGIN.getMsg());
        DataDimension dataDimension = new DataDimension();
        dataDimension.setAttributeName_(dataDimensionVO.getAttributeName()).setDisplayName_(dataDimensionVO.getDisplayName()).setDescription_(dataDimensionVO.getDescription()).setGmtCreated_(DateUtils.getCurrentSecondIntValue()).setGmtModified(DateUtils.getCurrentSecondIntValue());
        dataDimension.setModifier(String.valueOf(authUser.getUserId()));
        dataDimensionService.save(dataDimension);
        return new RespResult();
    }

    @RequestMapping("/doModify")
    @ResponseBody
    @SysLog(SysLog.Operate.DIMENSION_NAME_UPDATE)
    public RespResult doModify(@Valid DataDimensionVO dataDimensionVO, BindingResult bingResult, HttpServletRequest request) {
        if (bingResult.hasErrors()) {
            return new RespResult(RespResult.RespCode.PARAM_EXCEPTION.getCode(), bingResult.getAllErrors().get(0).getDefaultMessage());
        }
        AuthUser authUser = authService.getAuthUser(request);
        Assert.notNull(authUser,RespResult.RespCode.NO_LOGIN.getMsg());
        DataDimension dataDimension = dataDimensionService.get(dataDimensionVO.getDimensionId());
        Assert.notNull(dataDimension,RespResult.RespCode.NO_ENTITY.getMsg());
        DataDimension originalObject =new DataDimension();
        BeanUtils.copyProperties(dataDimension,originalObject);
        OriginalObjectHolder.putOriginal(originalObject);
        dataDimension.setAttributeName_(dataDimensionVO.getAttributeName()).setDisplayName_(dataDimensionVO.getDisplayName()).setDescription_(dataDimensionVO.getDescription()).setGmtModified(DateUtils.getCurrentSecondIntValue());
        dataDimension.setModifier(String.valueOf(authUser.getUserId()));
        dataDimensionService.update(dataDimension);
        OriginalObjectHolder.putNew(dataDimension);
        return new RespResult();
    }


    @RequestMapping("/delete")
    @ResponseBody
    @SysLog(SysLog.Operate.DIMENSION_NAME_DELETE)
    public RespResult delete(Integer dimensionId, HttpServletRequest request) {
        Assert.notNull(dimensionId,"dimensionId不能为空");
        AuthUser authUser = authService.getAuthUser(request);
        Assert.notNull(authUser,RespResult.RespCode.NO_LOGIN.getMsg());
        DataDimension dataDimension = dataDimensionService.get(dimensionId);
        Assert.notNull(dataDimension,RespResult.RespCode.NO_ENTITY.getMsg());
        Assert.isTrue(dataDimensionValuesService.countDimensionValuesByDimensionId(dimensionId)==0,"有数据维度值已绑定该维度，无法删除");
        OriginalObjectHolder.putOriginal(dataDimension);
        dataDimension.setGmtModified(DateUtils.getCurrentSecondIntValue());
        dataDimension.setIsDeleted(1);
        dataDimension.setModifier(String.valueOf(authUser.getUserId()));
        dataDimensionService.updateSelective(dataDimension);
        return new RespResult();
    }



    @RequestMapping("/doModifyDimensionValue")
    @ResponseBody
    @SysLog(SysLog.Operate.DIMENSION_VALUE_UPDATE)
    public RespResult doModifyDimensionValue(@Valid DataDimensionValuesVO dataDimensionValuesVO, BindingResult bingResult, HttpServletRequest request) {
        if (bingResult.hasErrors()) {
            return new RespResult(RespResult.RespCode.PARAM_EXCEPTION.getCode(), bingResult.getAllErrors().get(0).getDefaultMessage());
        }
        DataDimensionValues dataDimensionValue = dataDimensionValuesService.get(dataDimensionValuesVO.getRecId());
        DataDimensionValues originalObject =new DataDimensionValues();
        BeanUtils.copyProperties(dataDimensionValue,originalObject);
        OriginalObjectHolder.putOriginal(originalObject);
        dataDimensionValue.setAttributeValue_(dataDimensionValuesVO.getAttributeValue()).setDisplayName_(dataDimensionValuesVO.getDisplayName());
        dataDimensionValuesService.updateSelective(dataDimensionValue);
        OriginalObjectHolder.putNew(dataDimensionValue);
        return new RespResult();
    }


    @RequestMapping("/deleteDimensionValue")
    @ResponseBody
    @SysLog(SysLog.Operate.DIMENSION_VALUE_DELETE)
    public RespResult deleteDimensionValue(Integer recId, HttpServletRequest request) {
        Assert.notNull(recId,"recId不能为空");
        AuthUser authUser = authService.getAuthUser(request);
        Assert.notNull(authUser,RespResult.RespCode.NO_LOGIN.getMsg());
        DataDimensionValues dataDimensionValue = dataDimensionValuesService.get(recId);
        Assert.notNull(dataDimensionValue,RespResult.RespCode.NO_ENTITY.getMsg());
        DataDimension dataDimension = dataDimensionService.get(dataDimensionValue.getDimensionId());
        Assert.isTrue( rolePermissionDao.countByLikeAttributeValueAndAttributeName(dataDimension.getAttributeName(),dataDimensionValue.getAttributeValue())==0,"数据维度值已经在使用，无法删除");
        OriginalObjectHolder.putOriginal(dataDimensionValue);
        dataDimensionValue.setGmtModified(DateUtils.getCurrentSecondIntValue());
        dataDimensionValue.setIsDeleted(1);
        dataDimensionValue.setModifier(String.valueOf(authUser.getUserId()));
        dataDimensionValuesService.updateSelective(dataDimensionValue);
        return new RespResult();
    }



    @RequestMapping("/getDataDimensions")
    @ResponseBody
    public RespResult getDataDimensions(Integer menuId) {
        List<DataDimension> dataDimensionList = new ArrayList<>();
        if (menuId == null) {
            dataDimensionList = dataDimensionService.queryList(null);
        } else {
            Set<Integer> set = new HashSet<>();
            set.add(menuId);
            List<com.jolly.upms.manager.vo.DataDimensionVO> dataDimensionVOS = menuDao.queryMenuDataDimensions(set);
            for (com.jolly.upms.manager.vo.DataDimensionVO dataDimensionVO : dataDimensionVOS) {
                DataDimension dataDimension = new DataDimension();
                BeanUtils.copyProperties(dataDimensionVO, dataDimension);
                dataDimensionList.add(dataDimension);
            }
        }
        return new RespResult(dataDimensionList);
    }

}
