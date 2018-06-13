package com.hhly.file.service;

import com.hhly.file.param.MultipartFileParam;

import java.io.IOException;

/**
 * 存储操作的service
 *
 * @author BSW
 * @create 2017-09-27
 *
 */
public interface UpAndDownLoadService {

    /**
     * 删除全部数据
     */
    void deleteAll();

    /**
     * 初始化方法
     */
    void init();


    /**
     * 上传文件方法
     * 处理文件分块，基于MappedByteBuffer来实现文件的保存
     *
     * @param param
     * @throws IOException
     */
    void uploadFileByMappedByteBuffer(MultipartFileParam param) throws IOException;


    /**
     * @desc 根据资源ID获取资源路径（全路径）
     * @author BSW
     * @create 2017-09-29
     * @param resourceId
     * @return
     */
    String getResourcePathById(String resourceId);
}
