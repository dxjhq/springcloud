package org.mybatis.generator;

import org.mybatis.generator.api.ShellRunner;

/**
 * @author wangxianchen
 * @create 2017-09-06
 * @desc 启动类
 */
public class Run {
/*    public static void main(String[] args) {

        // 调试初始化参数
        Run run = new Run();
        //取得根目录路径
        String rootPath = run.getClass().getResource("/").getFile().toString();
        //当前目录的上级目录路径
        String[] arg = new String[]{"-configfile", rootPath + "generatorConfig_template.xml", "-overwrite"};

        ShellRunner.main(arg);
    }*/

    /**
     * @desc 启动方法
     * @author wangxianchen
     * @create 2018-01-16
     * @param configfilePath
     */
    public static void startUp(String configfilePath){
        String[] arg = new String[]{"-configfile", configfilePath, "-overwrite"};
        ShellRunner.main(arg);
    }
}
