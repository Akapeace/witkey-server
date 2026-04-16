package com.witkey.admin.service;

import com.witkey.admin.model.vo.category.AddCategoryReqVO;
import com.witkey.common.utils.Response;

/**
 * @author peace
 * @date 2026/4/16 17:43
 * @description:
 */
public interface AdminCategoryService {
    /**
     * 添加分类
     * @param addCategoryReqVO
     * @return
     */
    Response addCategory(AddCategoryReqVO addCategoryReqVO);
}

