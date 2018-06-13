package org.mybatis.generator.plugins;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangxianchen
 * @create 2017-09-06
 * @desc 添加DAO的实现
 */
public class MybatisDaoPlugin extends PluginAdapter {

    private FullyQualifiedJavaType daoType;

    private FullyQualifiedJavaType mapperType;

    private FullyQualifiedJavaType superType;

    private FullyQualifiedJavaType pojoType;

    private FullyQualifiedJavaType repository;

    private String pojoUrl;

    private String mapperUrl;

    private String superClass;

    private String project;

    private String servicePack;


    /**
     * @desc 校验
     * @author wangxianchen
     * @create 2017-09-07
     * @param warnings
     *            add strings to this list to specify warnings. For example, if
     *            the plugin is invalid, you should specify why. Warnings are
     *            reported to users after the completion of the run.
     * @return
     */
    @Override
    public boolean validate(List<String> warnings) {
        superClass = properties.getProperty("superClass");
        servicePack = properties.getProperty("targetPackage");
        project = properties.getProperty("targetProject");
        repository = new FullyQualifiedJavaType("org.springframework.stereotype.Repository");
        pojoUrl = context.getJavaModelGeneratorConfiguration().getTargetPackage();
        mapperUrl = context.getJavaClientGeneratorConfiguration().getTargetPackage();
        return true;
    }

    /**
     * @desc 生成java文件
     * @author wangxianchen
     * @create 2017-09-07
     * @param introspectedTable
     * @return
     */
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> files = new ArrayList<GeneratedJavaFile>();
        String table = introspectedTable.getBaseRecordType();
        String tableName = table.replaceAll(this.pojoUrl + ".", "");
        daoType = new FullyQualifiedJavaType(servicePack + "." + tableName + "Dao");
        superType = new FullyQualifiedJavaType(superClass);
        pojoType = new FullyQualifiedJavaType(pojoUrl + "." + tableName);
        mapperType = new FullyQualifiedJavaType(mapperUrl + "." + tableName+"Mapper");
        superType.addTypeArgument(mapperType);
        superType.addTypeArgument(pojoType);

        TopLevelClass topLevelClass = new TopLevelClass(daoType);
        topLevelClass.setSuperClass(superType);
        // 导入必要的类
        addImport(topLevelClass);
        addClassComment(topLevelClass);
        GeneratedJavaFile file = new GeneratedJavaFile(topLevelClass,project, context.getJavaFormatter());
        files.add(file);
        return files;
    }

    /**
     * @desc 导入需要的类
     * @author wangxianchen
     * @create 2017-09-07
     * @param topLevelClass
     */
    private void addImport(TopLevelClass topLevelClass) {
        topLevelClass.addImportedType(pojoType);
        topLevelClass.addImportedType(mapperType);
        topLevelClass.addImportedType(superType);
        topLevelClass.addImportedType(repository);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
    }

    /**
     * @desc 添加注释
     * @author wangxianchen
     * @create 2017-09-07
     * @param topLevelClass
     */
    private void addClassComment(TopLevelClass topLevelClass){
        topLevelClass.addJavaDocLine("@Repository");
    }
}
