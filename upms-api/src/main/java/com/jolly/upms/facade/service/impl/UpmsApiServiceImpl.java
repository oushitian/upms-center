package com.jolly.upms.facade.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jolly.upms.facade.dto.*;
import com.jolly.upms.facade.service.UpmsApiService;
import com.jolly.upms.facade.util.EncryptAndDecryptUtil;
import com.jolly.upms.facade.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author mars
 * @create 2017-06-22 11:04 AM
 **/
public class UpmsApiServiceImpl implements UpmsApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsApiServiceImpl.class);

    private String baseUrl;

    //加密密钥
    private String key = "+7HI5RMMEUmMJG6AI2OIpQ==";

    @Override
    public BaseResponseDTO<List<MenuRespDTO>> getUserMenus(BaseRequestDTO requestDTO) {
        try {
            //非空校验
            checkBlank(requestDTO);
            String url = baseUrl + "getUserMenus";
            Map<String, String> params = new HashMap<>();
            params.put("token", requestDTO.getToken());
            params.put("appKey", requestDTO.getAppKey());
            //encrypt
            encrypt(requestDTO, params);
            String resp = HttpUtil.getUrlValue(url, params);
            return JSON.parseObject(resp, new TypeReference<BaseResponseDTO<List<MenuRespDTO>>>() {
            });
        } catch (Exception e) {
            LOGGER.error("getUserMenus接口异常，请求数据为：{}", JSON.toJSONString(requestDTO), e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @Override
    public BaseResponseDTO logout(BaseRequestDTO requestDTO) {
        try {
            //非空校验
            checkBlank(requestDTO);
            String url = baseUrl + "logout";
            Map<String, String> params = new HashMap<>();
            params.put("token", requestDTO.getToken());
            params.put("appKey", requestDTO.getAppKey());
            //encrypt
            encrypt(requestDTO, params);
            String resp = HttpUtil.getUrlValue(url, params);
            return JSON.parseObject(resp, BaseResponseDTO.class);
        } catch (Exception e) {
            LOGGER.error("logout接口异常，请求数据为：{}", JSON.toJSONString(requestDTO), e);
            return new BaseResponseDTO(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @Override
    public BaseResponseDTO accessToken(BaseRequestDTO requestDTO) {
        try {
            //非空校验
            checkBlank(requestDTO);
            String url = baseUrl + "accessToken";
            Map<String, String> params = new HashMap<>();
            params.put("token", requestDTO.getToken());
            params.put("appKey", requestDTO.getAppKey());
            //encrypt
            encrypt(requestDTO, params);
            String resp = HttpUtil.getUrlValue(url, params);
            return JSON.parseObject(resp, BaseResponseDTO.class);
        } catch (Exception e) {
            LOGGER.error("accessToken接口异常，请求数据为：{}", JSON.toJSONString(requestDTO), e);
            return new BaseResponseDTO(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @Override
    public BaseResponseDTO<Map<String, Set<String>>> getDataPermissions(PermissionReqDTO reqDTO) {
        try {
            //非空校验
            checkBlank(reqDTO);
            String url = baseUrl + "getDataPermissions";
            Map<String, String> params = new HashMap<>();
            params.put("token", reqDTO.getToken());
            params.put("appKey", reqDTO.getAppKey());
            params.put("url", reqDTO.getUrl());
            //encrypt
            encrypt(reqDTO, params);
            String resp = HttpUtil.getUrlValue(url, params);
            return JSON.parseObject(resp, new TypeReference<BaseResponseDTO<Map<String, Set<String>>>>() {
            });
        } catch (Exception e) {
            LOGGER.error("getDataPermissions接口异常，请求数据为：{}", JSON.toJSONString(reqDTO), e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @Override
    public BaseResponseDTO<List<BatchDataPermissionRespDTO>> batchGetDataPermissions(BatchDataPermissionReqDTO reqDTO) {
        try {
            //非空校验
            checkBlank(reqDTO);
            String url = baseUrl + "batchGetDataPermissions";
            Map<String, String> params = new HashMap<>();
            params.put("token", reqDTO.getToken());
            params.put("appKey", reqDTO.getAppKey());
            Set<String> urls = reqDTO.getUrls();
            StringBuilder sb = new StringBuilder();
            for (String s : urls) {
                sb.append(s).append(",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            params.put("urls", sb.toString());
            //encrypt
            encrypt(reqDTO, params);
            String resp = HttpUtil.getUrlValue(url, params);
            return JSON.parseObject(resp, new TypeReference<BaseResponseDTO<List<BatchDataPermissionRespDTO>>>() {
            });
        } catch (Exception e) {
            LOGGER.error("getDataPermissions接口异常，请求数据为：{}", JSON.toJSONString(reqDTO), e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @Override
    public BaseResponseDTO validatePermission(PermissionReqDTO reqDTO) {
        try {
            //非空校验
            checkBlank(reqDTO);
            String url = baseUrl + "validatePermission";
            Map<String, String> params = new HashMap<>();
            params.put("token", reqDTO.getToken());
            params.put("appKey", reqDTO.getAppKey());
            params.put("url", reqDTO.getUrl());
            //encrypt
            encrypt(reqDTO, params);
            String resp = HttpUtil.getUrlValue(url, params);
            return JSON.parseObject(resp, BaseResponseDTO.class);
        } catch (Exception e) {
            LOGGER.error("validatePermission接口异常，请求数据为：{}", JSON.toJSONString(reqDTO), e);
            return new BaseResponseDTO(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @Override
    public BaseResponseDTO<Set<String>> findRoleIds(BaseRequestDTO reqDTO) {
        try {
            //非空校验
            checkBlank(reqDTO);
            String url = baseUrl + "findRoleIds";
            Map<String, String> params = new HashMap<>();
            params.put("token", reqDTO.getToken());
            params.put("appKey", reqDTO.getAppKey());
            //encrypt
            encrypt(reqDTO, params);
            String resp = HttpUtil.getUrlValue(url, params);
            return JSON.parseObject(resp, new TypeReference<BaseResponseDTO<Set<String>>>() {
            });
        } catch (Exception e) {
            LOGGER.error("findRoleIds接口异常，请求数据为：{}", JSON.toJSONString(reqDTO), e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @Override
    public BaseResponseDTO<UserRespDTO> getUser(GetUserReqDTO reqDTO) {
        try {
            //非空校验
            checkBlank(reqDTO);
            String url = baseUrl + "getUser";
            Map<String, String> params = new HashMap<>();
            params.put("token", reqDTO.getToken());
            params.put("appKey", reqDTO.getAppKey());
            if (reqDTO.getUserId() != null) {
                params.put("userId", reqDTO.getUserId().toString());
                if (reqDTO.getIncludeDeleted() != null)
                    params.put("includeDeleted", reqDTO.getIncludeDeleted().toString());
            } else if (StringUtils.isNotBlank(reqDTO.getUserName())) {
                params.put("userName", reqDTO.getUserName());
            }
            //encrypt
            encrypt(reqDTO, params);
            String resp = HttpUtil.getUrlValue(url, params);
            return JSON.parseObject(resp, new TypeReference<BaseResponseDTO<UserRespDTO>>() {
            });
        } catch (Exception e) {
            LOGGER.error("getUser接口异常，请求数据为：{}", JSON.toJSONString(reqDTO), e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @Override
    public BaseResponseDTO<List<UserRespDTO>> queryUserByRoleIds(UserReqDTO reqDTO) {
        try {
            //非空校验
            checkBlank(reqDTO);
            String url = baseUrl + "queryUserByRoleIds";
            Map<String, String> params = new HashMap<>();
            params.put("token", reqDTO.getToken());
            params.put("appKey", reqDTO.getAppKey());
            Set<Integer> roleIds = reqDTO.getRoleIds();
            StringBuilder sb = new StringBuilder();
            for (Integer roleId : roleIds) {
                sb.append(roleId).append(",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            params.put("roleIds", sb.toString());
            //encrypt
            encrypt(reqDTO, params);
            String resp = HttpUtil.getUrlValue(url, params);
            return JSON.parseObject(resp, new TypeReference<BaseResponseDTO<List<UserRespDTO>>>() {
            });
        } catch (Exception e) {
            LOGGER.error("queryUserByRoleIds接口异常，请求数据为：{}", JSON.toJSONString(reqDTO), e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @Override
    public BaseResponseDTO<List<UserRoleRespDTO>> queryUserRoleByRoleIds(UserReqDTO reqDTO) {
        try {
            //非空校验
            checkBlank(reqDTO);
            String url = baseUrl + "queryUserRoleByRoleIds";
            Map<String, String> params = new HashMap<>();
            params.put("token", reqDTO.getToken());
            params.put("appKey", reqDTO.getAppKey());
            Set<Integer> roleIds = reqDTO.getRoleIds();
            StringBuilder sb = new StringBuilder();
            for (Integer roleId : roleIds) {
                sb.append(roleId).append(",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            params.put("roleIds", sb.toString());
            if (reqDTO.getUserId() != null)
                params.put("userId", reqDTO.getUserId().toString());
            //encrypt
            encrypt(reqDTO, params);
            String resp = HttpUtil.getUrlValue(url, params);
            return JSON.parseObject(resp, new TypeReference<BaseResponseDTO<List<UserRoleRespDTO>>>() {
            });
        } catch (Exception e) {
            LOGGER.error("queryUserRoleByRoleIds接口异常，请求数据为：{}", JSON.toJSONString(reqDTO), e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @Override
    public BaseResponseDTO<List<UserRespDTO>> queryUserByParams(QueryUserReqDTO reqDTO) {
        try {
            //非空校验
            checkBlank(reqDTO);
            String url = baseUrl + "queryUserByParams";
            Map<String, String> params = new HashMap<>();
            params.put("token", reqDTO.getToken());
            params.put("appKey", reqDTO.getAppKey());
            if (StringUtils.isNotBlank(reqDTO.getUserName())) {
                params.put("userName", reqDTO.getUserName());
            }
            //encrypt
            encrypt(reqDTO, params);
            String resp = HttpUtil.getUrlValue(url, params);
            return JSON.parseObject(resp, new TypeReference<BaseResponseDTO<List<UserRespDTO>>>() {
            });
        } catch (Exception e) {
            LOGGER.error("queryUserByParams接口异常，请求数据为：{}", JSON.toJSONString(reqDTO), e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @Override
    public BaseResponseDTO resumeOrDelSuppUser(SuppUserReqDTO reqDTO) {
        try {
            //非空校验
            checkBlank(reqDTO);
            String url = baseUrl + "resumeOrDelSuppUser";
            Map<String, String> params = new HashMap<>();
            params.put("token", reqDTO.getToken());
            params.put("appKey", reqDTO.getAppKey());
            params.put("suppCode", reqDTO.getSuppCode());
            params.put("deleteUser", reqDTO.getDeleteUser().toString());
            //encrypt
            encrypt(reqDTO, params);
            String resp = HttpUtil.getUrlValue(url, params);
            return JSON.parseObject(resp, BaseResponseDTO.class);
        } catch (Exception e) {
            LOGGER.error("resumeOrDelSuppUser接口异常，请求数据为：{}", JSON.toJSONString(reqDTO), e);
            return new BaseResponseDTO(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @Override
    public BaseResponseDTO<Set<String>> getPermissions(PermReqDTO reqDTO) {
        try {
            //非空校验
            checkBlank(reqDTO);
            String url = baseUrl + "getPermissions";
            Map<String, String> params = new HashMap<>();
            params.put("token", reqDTO.getToken());
            params.put("appKey", reqDTO.getAppKey());
            Byte menuType = reqDTO.getMenuType();
            String parentMenuUrl = reqDTO.getParentMenuUrl();
            if (menuType != null) {
                params.put("menuType", menuType.toString());
            }
            if (StringUtils.isNotBlank(parentMenuUrl)) {
                params.put("parentMenuUrl", parentMenuUrl);
            }
            //encrypt
            encrypt(reqDTO, params);
            String resp = HttpUtil.getUrlValue(url, params);
            return JSON.parseObject(resp, new TypeReference<BaseResponseDTO<Set<String>>>() {
            });
        } catch (Exception e) {
            LOGGER.error("getPermissions接口异常，请求数据为：{}", JSON.toJSONString(reqDTO), e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @Override
    public BaseResponseDTO addFavorite(FavoriteReqDTO reqDTO) {
        try {
            //非空校验
            checkBlank(reqDTO);
            String url = baseUrl + "addFavorite";
            Map<String, String> params = new HashMap<>();
            params.put("token", reqDTO.getToken());
            params.put("appKey", reqDTO.getAppKey());
            Set<Integer> menuIds = reqDTO.getMenuIds();
            StringBuilder sb = new StringBuilder();
            for (Integer menuId : menuIds) {
                sb.append(menuId).append(",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            params.put("menuIds", sb.toString());
            //encrypt
            encrypt(reqDTO, params);
            String resp = HttpUtil.getUrlValue(url, params);
            return JSON.parseObject(resp, BaseResponseDTO.class);
        } catch (Exception e) {
            LOGGER.error("addFavorite接口异常，请求数据为：{}", JSON.toJSONString(reqDTO), e);
            return new BaseResponseDTO(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @Override
    public BaseResponseDTO<List<FavoriteRespDTO>> queryFavorites(BaseRequestDTO reqDTO) {
        try {
            //非空校验
            checkBlank(reqDTO);
            String url = baseUrl + "queryFavorites";
            Map<String, String> params = new HashMap<>();
            params.put("token", reqDTO.getToken());
            params.put("appKey", reqDTO.getAppKey());
            //encrypt
            encrypt(reqDTO, params);
            String resp = HttpUtil.getUrlValue(url, params);
            return JSON.parseObject(resp, new TypeReference<BaseResponseDTO<List<FavoriteRespDTO>>>() {
            });
        } catch (Exception e) {
            LOGGER.error("queryFavorites接口异常，请求数据为：{}", JSON.toJSONString(reqDTO), e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @Override
    public BaseResponseDTO deleteFavorite(FavoriteReqDTO reqDTO) {
        try {
            //非空校验
            checkBlank(reqDTO);
            String url = baseUrl + "deleteFavorite";
            Map<String, String> params = new HashMap<>();
            params.put("token", reqDTO.getToken());
            params.put("appKey", reqDTO.getAppKey());
            Set<Integer> menuIds = reqDTO.getMenuIds();
            StringBuilder sb = new StringBuilder();
            for (Integer menuId : menuIds) {
                sb.append(menuId).append(",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            params.put("menuIds", sb.toString());
            //encrypt
            encrypt(reqDTO, params);
            String resp = HttpUtil.getUrlValue(url, params);
            return JSON.parseObject(resp, BaseResponseDTO.class);
        } catch (Exception e) {
            LOGGER.error("deleteFavorite接口异常，请求数据为：{}", JSON.toJSONString(reqDTO), e);
            return new BaseResponseDTO(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @Override
    public BaseResponseDTO<UserRespDTO> getUserWithoutToken(SimpleUserReqDTO reqDTO) {
        try {
            //非空校验
            checkBlank(reqDTO);
            String url = baseUrl + "getUserWithoutToken";
            Map<String, String> params = new HashMap<>();
            params.put("appKey", reqDTO.getAppKey());
            params.put("userId", reqDTO.getUserId().toString());
            //encrypt
            encrypt(reqDTO, params);
            String resp = HttpUtil.getUrlValue(url, params);
            return JSON.parseObject(resp, new TypeReference<BaseResponseDTO<UserRespDTO>>() {
            });
        } catch (Exception e) {
            LOGGER.error("getUserWithoutToken接口异常，请求数据为：{}", JSON.toJSONString(reqDTO), e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    @Override
    public BaseResponseDTO<List<DataDimensionValuesRespDTO>> queryDataDimensionValues(DataDimensionValuesReqDTO reqDTO) {
        try {
            //非空校验
            checkBlank(reqDTO);
            String url = baseUrl + "queryDataDimensionValues";
            Map<String, String> params = new HashMap<>(2);
            params.put("appKey", reqDTO.getAppKey());
            params.put("dimensionAttributeName", reqDTO.getDimensionAttributeName());
            //encrypt
            encrypt(reqDTO, params);
            String resp = HttpUtil.getUrlValue(url, params);
            return JSON.parseObject(resp, new TypeReference<BaseResponseDTO<List<DataDimensionValuesRespDTO>>>() {
            });
        } catch (Exception e) {
            LOGGER.error("queryDataDimensionValues接口异常，请求数据为：{}", JSON.toJSONString(reqDTO), e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    private void encrypt(Object obj, Map<String, String> params) throws Exception {
        String plain = JSON.toJSONString(obj);
        String sign = EncryptAndDecryptUtil.encrypt(key, plain);
        params.put("plain", plain);
        params.put("sign", sign);
    }

    private void checkBlank(Object obj) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> set = validator.validate(obj);
        for (ConstraintViolation<Object> violation : set) {
            throw new RuntimeException(violation.getPropertyPath() + violation.getMessage());
        }
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl + "/api/";
    }

    public void setKey(String key) {
        this.key = key;
    }
}
