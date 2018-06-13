package com.hhly.file.service.impl;

import com.hhly.file.param.MultipartFileParam;
import com.hhly.file.service.UpAndDownLoadService;
import com.hhly.file.utils.Constants;
import com.hhly.file.utils.FileMD5Util;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 上传文件逻辑处理
 * @author BSW
 * @create 2017-09-28
 * @desc
 *
 */
@Service
public class UpAndDownLoadServiceImpl implements UpAndDownLoadService {

    private final Logger logger = LoggerFactory.getLogger(UpAndDownLoadServiceImpl.class);
    // 保存文件的根目录
    private Path rootPaht;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //这个必须与前端设定的值一致
    @Value("${breakpoint.upload.chunkSize}")
    private long CHUNK_SIZE;

    @Value("${breakpoint.upload.dir}")
    private String finalDirPath;

    @Autowired
    public UpAndDownLoadServiceImpl(@Value("${breakpoint.upload.dir}") String location) {
        this.rootPaht = Paths.get(location);
    }

    @Override
    public void deleteAll() {
        logger.info("开发初始化清理数据，start");
//        FileSystemUtils.deleteRecursively(rootPaht.toFile());
//        stringRedisTemplate.delete(Constants.FILE_UPLOAD_STATUS);
//        stringRedisTemplate.delete(Constants.FILE_MD5_KEY);
        logger.info("开发初始化清理数据，end");
    }

    @Override
    public void init() {
            File file = rootPaht.toFile();
            boolean exist = file.exists();
            if(!exist){
                file.mkdirs();
            }else{
                logger.info("文件夹已经存在了，不用再创建。");
            }


    }

    /**
     * 使用管道 缓冲区方式进行读写
     * @author BSW
     * @create 2017-10-17
     * @param param
     * @throws IOException
     */
    @Override
    public void uploadFileByMappedByteBuffer(MultipartFileParam param) throws IOException {
        //获取上传文件名称
        String fileName = param.getName();
        //拼接文件上传路径 --E:/data/uploads/  + md5 串
        String uploadDirPath = finalDirPath + param.getMd5();
        //拼接 上传进行过程中的临时文件名
        String tempFileName = fileName + "_tmp";
        File tmpDir = new File(uploadDirPath);
        //创建文件
        File tmpFile = new File(uploadDirPath, tempFileName);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        //创建随机读写数据流对象--特性很强大
        RandomAccessFile tempRaf = new RandomAccessFile(tmpFile, "rw");
        FileChannel fileChannel = tempRaf.getChannel();

        /**
         * 写入该分片数据
         * 计算流写入的开始位置-文件中的位置，映射区域从此位置开始；必须为非负数
         * 这里的chunk 参数是从零开始所以没有减一
        */
        long offset = CHUNK_SIZE * param.getChunk();
        //由于是分块上传的，这里接收的也是具有分块标识的数据块对应的流对象
        byte[] fileData = param.getFile().getBytes();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, fileData.length);
        mappedByteBuffer.put(fileData);
        // 释放ByteBuffer
        FileMD5Util.freedMappedByteBuffer(mappedByteBuffer);
        fileChannel.close();

        //检查当前流块的写入是否是最后一个了，如果是最后一个那么代表文件已经全部写完
        boolean isOk = checkAndSetUploadProgress(param, uploadDirPath);
        //如果写完了，则修改文件名称为原文件名称（上传时的文件名称）
        if (isOk) {
            boolean flag = renameFile(tmpFile, fileName);
            System.out.println("upload complete !!" + flag + " name=" + fileName);
        }
    }

    /**
     * 检查并修改文件上传进度
     *
     * @param param
     * @param uploadDirPath
     * @return
     * @throws IOException
     */
    private boolean checkAndSetUploadProgress(MultipartFileParam param, String uploadDirPath) throws IOException {
        String fileName = param.getName();
        File confFile = new File(uploadDirPath, fileName + ".conf");
        RandomAccessFile accessConfFile = new RandomAccessFile(confFile, "rw");
        //把该分段标记为 true 表示完成
        System.out.println("set part " + param.getChunk() + " complete");
        accessConfFile.setLength(param.getChunks());
        accessConfFile.seek(param.getChunk());
        accessConfFile.write(Byte.MAX_VALUE);

        //completeList 检查是否全部完成,如果数组里是否全部都是(全部分片都成功上传)
        byte[] completeList = FileUtils.readFileToByteArray(confFile);
        byte isComplete = Byte.MAX_VALUE;
        for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {
            //与运算, 如果有部分没有完成则 isComplete 不是 Byte.MAX_VALUE
            isComplete = (byte) (isComplete & completeList[i]);
            System.out.println("check part " + i + " complete?:" + completeList[i]);
        }

        accessConfFile.close();
        if (isComplete == Byte.MAX_VALUE) {
            stringRedisTemplate.opsForHash().put(Constants.FILE_UPLOAD_STATUS, param.getMd5(), "true");
            stringRedisTemplate.opsForValue().set(Constants.FILE_MD5_KEY + param.getMd5(), uploadDirPath + "/" + fileName);
            return true;
        } else {
            if (!stringRedisTemplate.opsForHash().hasKey(Constants.FILE_UPLOAD_STATUS, param.getMd5())) {
                stringRedisTemplate.opsForHash().put(Constants.FILE_UPLOAD_STATUS, param.getMd5(), "false");
            }
            if (stringRedisTemplate.hasKey(Constants.FILE_MD5_KEY + param.getMd5())) {
                stringRedisTemplate.opsForValue().set(Constants.FILE_MD5_KEY + param.getMd5(), uploadDirPath + "/" + fileName + ".conf");
            }
            return false;
        }
    }

    /**
     * 文件重命名
     * @author BSW
     * @create 2017-10-17
     * @param toBeRenamed   将要修改名字的文件
     * @param toFileNewName 新的名字
     * @return
     */
    public boolean renameFile(File toBeRenamed, String toFileNewName) {
        //检查要重命名的文件是否存在，是否是文件
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {
            logger.info("File does not exist: " + toBeRenamed.getName());
            return false;
        }
        String p = toBeRenamed.getParent();
        File newFile = new File(p + File.separatorChar + toFileNewName);
        //修改文件名
        return toBeRenamed.renameTo(newFile);
    }

    /**
     * @desc 根据资源ID获取资源路径（全路径）
     * @author BSW
     * @create 2017-09-29
     * @param resourceId
     * @return
     */
    @Override
    public String getResourcePathById(String resourceId){
        if(stringRedisTemplate.hasKey(Constants.FILE_MD5_KEY+resourceId)){
            return stringRedisTemplate.opsForValue().get(Constants.FILE_MD5_KEY+resourceId);
        }else{
            return null;
        }

    }

    public static void main(String[] args){
        Path rootPaht = Paths.get("D:/data/uploads/");
        File file = rootPaht.toFile();
        boolean exist = file.exists();
        boolean isDirectory = rootPaht.toFile().isDirectory();
        System.out.println(exist);
        System.out.println(isDirectory);
    }
}
