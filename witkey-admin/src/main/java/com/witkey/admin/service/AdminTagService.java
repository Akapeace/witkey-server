package com.witkey.admin.service;

import com.witkey.admin.model.vo.tag.AddTagReqVO;
import com.witkey.admin.model.vo.tag.DeleteTagReqVO;
import com.witkey.admin.model.vo.tag.FindTagPageListReqVO;
import com.witkey.admin.model.vo.tag.SearchTagsReqVO;
import com.witkey.common.utils.PageResponse;
import com.witkey.common.utils.Response;

public interface AdminTagService {

    /**
     * 添加标签集合
     * @param addTagReqVO
     * @return
     */
    Response addTags(AddTagReqVO addTagReqVO);

    /**
     * 查询标签分页
     * @param findTagPageListReqVO
     * @return
     */
    PageResponse findTagPageList(FindTagPageListReqVO findTagPageListReqVO);

    /**
     * 删除标签
     * @param deleteTagReqVO
     * @return
     */
    Response deleteTag(DeleteTagReqVO deleteTagReqVO);

    /**
     * 根据标签关键词模糊查询
     * @param searchTagsReqVO
     * @return
     */
    Response searchTags(SearchTagsReqVO searchTagsReqVO);
}
