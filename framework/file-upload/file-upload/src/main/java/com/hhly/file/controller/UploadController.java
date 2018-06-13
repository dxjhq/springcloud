package com.hhly.file.controller;

import com.hhly.file.param.MultipartFileParam;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.hhly.file.service.UpAndDownLoadService;
import com.hhly.file.utils.Constants;
import com.hhly.file.vo.ResultStatus;
import com.hhly.file.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * 控制层--秒传  上传  下载
 *
 * @author BSW
 * @create 2017-09-28
 * @desc
 */

@Controller
@RequestMapping(value = "/file")
public class UploadController {

    private Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UpAndDownLoadService upAndDownLoadService;

    /**
     * 秒传判断，断点判断
     *
     * @return
     */
    @RequestMapping(value = "/checkFileMd5", method = RequestMethod.POST)
    @ResponseBody
    public Object checkFileMd5(String md5) throws IOException {
        Object processingObj = stringRedisTemplate.opsForHash().get(Constants.FILE_UPLOAD_STATUS, md5);
        if (processingObj == null) {
            return new ResultVo(ResultStatus.NO_HAVE);
        }
        String processingStr = processingObj.toString();
        boolean processing = Boolean.parseBoolean(processingStr);
        String value = stringRedisTemplate.opsForValue().get(Constants.FILE_MD5_KEY + md5);
        if (processing) {
            return new ResultVo(ResultStatus.IS_HAVE, value);
        } else {
            File confFile = new File(value);
            byte[] completeList = FileUtils.readFileToByteArray(confFile);
            List<String> missChunkList = new LinkedList<>();
            for (int i = 0; i < completeList.length; i++) {
                if (completeList[i] != Byte.MAX_VALUE) {
                    missChunkList.add(i + "");
                }
            }
            return new ResultVo<>(ResultStatus.ING_HAVE, missChunkList);
        }
    }

    /**
     * 上传文件
     *
     * @param param
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity fileUpload(MultipartFileParam param, HttpServletRequest request) {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            logger.info("上传文件开始。");
            try {
                // 使用缓冲区方式写
                upAndDownLoadService.uploadFileByMappedByteBuffer(param);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("文件上传失败。{}", param.toString());
            }
            logger.info("上传文件结束。");
        }
        return ResponseEntity.ok().body("上传成功。");
    }



    /**
     * 上传下载
     *
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fileDownload")
    @ResponseBody
    public void fileDownload(String userId,String picId, HttpServletResponse response) throws IOException {
        //鉴权处理TODO：
        //@Valid @RequestBody DownloadParam param,
        //有权限则放行

        String url = upAndDownLoadService.getResourcePathById(picId);
        File file = new File(url);
        String fileName = file.getName();

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        response.setContentType("application/octet-stream");
        //TODO: 暂时使用此流，如果需要下载也分片，换成 随机读写流
        InputStream is = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(is,response.getOutputStream());

    }

}
