package com.witkey.admin.service;

import com.witkey.admin.model.vo.UpdateAdminUserPasswordReqVO;
import com.witkey.common.utils.Response;

/**
 * @author peace
 * @date 2026/4/1 17:56
 * @description:
 */
public interface AdminUserService {
    /**
     * 修改密码
     * @param updateAdminUserPasswordReqVO
     * @return
     */
    Response updatePassword(UpdateAdminUserPasswordReqVO updateAdminUserPasswordReqVO);
}

