package com.hhly.utils.file;

import com.hhly.utils.ValueUtil;
import com.hhly.utils.LogUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.im4java.core.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

/**
 * 文件处理工具类
 * Created by xiaoss on 2016/8/22 0022.
 */
@SuppressWarnings("ALL")
public class FileUtil {

    @Setter
    @Getter
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class FileInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        /** 需要回显给前端时, 只返回指定的属性 */
        public static final String[] RETURN_PROPERTIES = new String[] {"name", "size", "width", "height", "url"};
        /**前端上传趣闻图片的URL**/
        public static final String[] RETURN_NEW = new String[]{ "url" };

        /** 文件名 */
        private String name;
        /** 文件路径 */
        private String path;
        /** 人类可读的文件大小 */
        private String size;
        /** 文件宽度 */
        private int width;
        /** 文件高度 */
        private int height;
        /** 文件的访问路径 */
        private String url;

        /** 文件的 md5 值 */
        //public String getMd5() {
        //    return U.isNotBlank(path) ? Encrypt.toMd5File(new File(path)) : U.EMPTY;
        //}
    }

    // ========== 目录 ==========
    /** 后台上传 word 文书的存放目录 */
    public static final String DOC = "doc";
    /** 用户(包括用户和律师)上传文件时的存放目录. 可以再拼接上「用户 id」 */
    private static final String USER = "user";
    /** 用户(律师)上传的私密一点的文件存放目录, 如银行卡, 身份证等. 可以再拼接上「用户 id」 */
    private static final String SECURITY = "security";

    /** 后台管理时添加的文件存放目录, 里面的目录对应场景. 如 advice(案件类型图), book(文书类型图) */
    private static final String CONFIG = "config";

    /** 用户上传文章时的图片存放目录名 或 运营在后台添加探索文章时的图片存放目录名, 两个场景只是目录名相同 */
    private static final String ARTICLE = "article";
    /** 运营在后台上传的轮播图的存放目录 */
    private static final String ROTATION = "rotation";
    /** 运营在后台上传案件类型时的图片存放目录 */
    private static final String ADVICE = "advice";
    /** 运营在后台上传文章时的图片存放目录 */
    private static final String PACKAGE = "package";
    /** 运营在后台上传的律师端轮播图的存放目录 */
    private static final String LAWYER_ROTATION="lawyerRotation";
    /** 用户上传趣闻帖子时的图片存放目录名 */
    private static final String NEW = "new";
    // =========================

    /**
     * 后台上传文书(word, doc 或 docx 格式)
     *
     * @param file   文件对象
     * @param path   文件存放的路径
     * @param domain 文件能被访问到的域名前缀
     * @return @see {@link FileInfo}
     */
    public static FileInfo uploadDocFile ( MultipartFile file, String path, String domain ) {
        return uploadFile( file, path, domain, DOC, ValueUtil.EMPTY );
    }

    /**
     * 用户上传的文件. 如用户头像等等
     *
     * @param file 文件对象
     * @param path 文件存放的路径
     * @param domain 文件能被访问到的域名前缀
     * @param userId 存放路径再加一层用户 id
     * @return 上传成功后可被访问到的地址
     */
    public static String uploadUserFile(MultipartFile file, String path, String domain, Long userId) {
        return uploadAndReturnUrl(file, path, domain, USER, String.valueOf(userId));
    }

    /**
     * 用户(律师)上传文章
     *
     * @param file 文件对象
     * @param path 文件存放的路径
     * @param domain 文件能被访问到的域名前缀
     * @param userId 存放路径再加一层用户 id
     * @return 上传成功后的文件信息
     */
    public static FileInfo uploadUserArticleFile(MultipartFile file, String path, String domain, Long userId) {
        return uploadFile(file, path, domain, USER, ValueUtil.addSuffix(String.valueOf(userId)) + ARTICLE);
    }

    /**
     * 用户(律师)上传趣闻帖子
     *
     * @param file 文件对象
     * @param path 文件存放的路径
     * @param domain 文件能被访问到的域名前缀
     * @param userId 存放路径再加一层用户 id
     * @return 上传成功后的文件信息
     */
    public static String uploadUserNewFile(MultipartFile file, String path, String domain, Long userId) {
        return uploadAndReturnUrl(file, path, domain, USER, ValueUtil.addSuffix(String.valueOf(userId)) + ARTICLE);
    }

    /**
     * 用户(律师)上传的私密文件. 如身份证银行卡等
     *
     * @param file 文件对象
     * @param path 文件存放的路径
     * @param domain 文件能被访问到的域名前缀
     * @param userId 存放路径再加一层用户 id
     * @return 上传成功后可被访问到的地址
     */
    public static String uploadSecurityFile(MultipartFile file, String path, String domain, Long userId) {
        return uploadAndReturnUrl(file, path, domain, SECURITY, String.valueOf(userId));
    }

    /** 运营在后台上传的用户端的轮播图 */
    public static String uploadRotationConfigFile(MultipartFile file, String path, String domain) {
        return uploadConfigFile(file, path, domain, ROTATION);
    }
    /** 运营在后台添加的案件类型的图片 */
    public static String uploadAdviceConfigFile(MultipartFile file, String path, String domain) {
        return uploadConfigFile(file, path, domain, ADVICE);
    }
    /** 运营在后台添加的 android 的安装包 */
    public static String uploadPackageConfigFile(MultipartFile file, String path, String domain) {
        return uploadConfigFile(file, path, domain, PACKAGE);
    }
    /** 运营在后台上传的探索文章里的图片. 运营上传的探索文章图片 与 用户上传的文章图片 目录名一样 */
    public static String uploadArticleConfigFile(MultipartFile file, String path, String domain) {
        return uploadConfigFile(file, path, domain, ARTICLE);
    }
    /** 运营在后台上传的律师端的轮播图 */
    public static String uploadLawyerRotationConfigFile(MultipartFile file, String path, String domain){
        return uploadConfigFile(file, path, domain, LAWYER_ROTATION);
    }

  

    /**
     * 上传一些公共文件等
     *
     * @param file 文件对象
     * @param path 文件存放的路径
     * @param domain 文件能被访问到的域名前缀
     * @param dir 文件存放的路径再加的目录
     * @return 上传成功后可被访问到的地址
     */
    private static String uploadConfigFile(MultipartFile file, String path, String domain, String dir) {
        return uploadAndReturnUrl(file, path, domain, CONFIG, dir);
    }

    private static String uploadAndReturnUrl(MultipartFile file, String path, String domain, String parent, String dir) {
        FileInfo fileInfo = uploadFile(file, path, domain, parent, dir);
        return ValueUtil.isBlank(fileInfo) ? ValueUtil.EMPTY : fileInfo.getUrl();
    }
    /** 图片上传. 上传成功后将文件信息返回 */
    private static FileInfo uploadFile(MultipartFile file, String path, String domain, String parent, String dir) {
//        U.assertNil(file, "上传文件为空");
//        U.assertNil(path, "上传文件要给一个存放的地方");
//        U.assertNil(domain, "上传文件要给一个能被访问到的域名. 与存放地相对应");

        // 文件的存放目录
        String filePath = String.format("%s%s%s", ValueUtil.addSuffix(path), ValueUtil.addSuffix(parent), dir);
        // 文件的域名, 与上面的目录相对应
        String address = String.format("%s%s%s", ValueUtil.addSuffix(domain), ValueUtil.addSuffix(parent), dir);

        FileInfo info = null;
        try {
            // 建目录
            filePath = ValueUtil.addSuffix(filePath);
            File saveFile = new File(filePath);
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }
            saveFile = new File(filePath, ValueUtil.renameFile(file.getOriginalFilename()));
            file.transferTo(saveFile);
            // 文件信息
            info = infomation(saveFile);

            // 把文件进行压缩
            FileInfo compress = imageCompress(saveFile);
            if (ValueUtil.isNotBlank(compress)) info = compress;

            // 设置 url 地址及原文件名并返回
            return info.setUrl(ValueUtil.addSuffix(address) + info.getName()).setName(file.getOriginalFilename());
        } catch (FileNotFoundException fe) {
            if (LogUtil.ROOT_LOG.isInfoEnabled())
                LogUtil.ROOT_LOG.info("文件路径未找到：" + filePath);
        } catch (Exception e) {
            if (LogUtil.ROOT_LOG.isInfoEnabled())
                LogUtil.ROOT_LOG.info("上传图片异常", e);
        }
        return info;
    }
    /** 基于 文件 返回除了 url 外的 文件名, 路径, 大小, 高宽 等信息, 其中高宽只在文件是图片时才会有 */
    private static FileInfo infomation(File file) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setName(file.getName())
                .setPath(file.getPath())
                .setSize(ValueUtil.humanRead(file.length()));
        // 图片才会有高宽
        if (ValueUtil.checkImage(file.getName())) {
            try {
                Info info = new Info(file.getPath());
                fileInfo.setWidth(info.getImageWidth()).setHeight(info.getImageHeight());
            } catch (InfoException e) {
                if (LogUtil.ROOT_LOG.isInfoEnabled())
                    LogUtil.ROOT_LOG.info(String.format("获取文件(%s)信息时异常", file.getPath()), e);
            }
        }
        return fileInfo;
    }

    /** 压缩文件的大小: 超过了就进行压缩 */
    private static final Number COMPRESS_FILE_SIZE_KB = 300;
    private static final String JPG_COMPRESS = "jpeg:extent=" + COMPRESS_FILE_SIZE_KB + "kb";
    /** 图片大小超过 300kb 则进行压缩, 并将压缩后的文件信息返回 */
    private static FileInfo imageCompress(File file) {
        if (ValueUtil.isNotBlank(file) && ValueUtil.checkImage(file.getName())) {
            // 右移 10 位相关于除以 1024, 相当于从字节到 kb, 只是没有小数位了
            long length = file.length() >> 10;
            // 指定大小以上的图才压缩
            if (length > COMPRESS_FILE_SIZE_KB.longValue()) {
                try {
                    // 压缩后的文件全路径
                    String compressFile = ValueUtil.addSuffix(file.getParent())
                            + ValueUtil.getPrefix(file.getName()) + "_lite" + ValueUtil.getSuffix(file.getName());

                    IMOperation operation = new IMOperation();
                    operation.addImage();

                    // http://stackoverflow.com/questions/6917219/imagemagick-scale-jpeg-image-with-a-maximum-file-size
                    // http://www.imagemagick.org/script/command-line-options.php#define
                    // 当图片是 jpg jpeg 格式时, 直接限制压缩后的图片大小上限
                    operation.define(JPG_COMPRESS);

                    // 压缩百分比
                    int scale = Double.valueOf(COMPRESS_FILE_SIZE_KB.doubleValue() * 100 / length).intValue();
                    operation.scale().addRawArgs(scale + "%");

                    operation.addImage();
                    new ConvertCmd().run(operation, file.getPath(), compressFile);
                    return infomation(new File(compressFile));
                } catch (IOException | InterruptedException | IM4JavaException e) {
                    if (LogUtil.ROOT_LOG.isInfoEnabled())
                        LogUtil.ROOT_LOG.info("压缩文件时异常", e);
                }
            }
        }
        return null;
    }

    /**
     * 图片重命名
     * @param path  文件路径
     * @param oldName  要修改的文件名
     */
    public static String renameImage(String path,String oldName) {
        try {
            String newName = ValueUtil.uuid() + ValueUtil.getSuffix(oldName);
            File file = new File(path+oldName); // 获取旧的文件
            File newFile = new File(path+newName); // 创建新的文件
            if(newFile.exists()){
                newFile.delete();
            }
            if(file.renameTo(newFile)) {  //文件重命名
                if(file.exists()) {  //删除上传的源文件
                    file.delete();
                }
                return newFile.getName();
            }
        } catch (Exception e) {
            if (LogUtil.ROOT_LOG.isInfoEnabled())
                LogUtil.ROOT_LOG.info("重命名文件时异常：", e);
        }
        return null;
    }
}
