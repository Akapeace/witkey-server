package com.witkey.admin.service;

import com.witkey.admin.model.vo.category.AddCategoryReqVO;
import com.witkey.admin.model.vo.category.DeleteCategoryReqVO;
import com.witkey.admin.model.vo.category.FindCategoryPageListReqVO;
import com.witkey.common.utils.PageResponse;
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
    /**
     * 分类分页数据查询
     * @param findCategoryPageListReqVO
     * @return
     */
    PageResponse findCategoryList(FindCategoryPageListReqVO findCategoryPageListReqVO);


    /**
     * 删除分类
     * @param deleteCategoryReqVO
     * @return
     */
    Response deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO);
}

