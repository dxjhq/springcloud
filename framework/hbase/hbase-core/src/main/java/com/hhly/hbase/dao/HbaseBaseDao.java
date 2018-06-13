package com.hhly.hbase.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hhly.hbase.common.HBaseResultBuilder;
import com.hhly.hbase.common.PutExtension;
import com.hhly.hbase.common.utils.StringUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Logger;

@Service
public class HbaseBaseDao<T>{
    private static final Logger log = Logger.getLogger(HBaseResultBuilder.class.getName());
    @Autowired
    public HbaseTemplate hbaseTemplate;


    /**
     * 封装下HbaseTemplate 中put方法
     * @param tableName 表名称
     * @param familyName 列簇名称
     * @param rowKey  暂时设定为UUID
     * @param  t 要插入的列簇对应实体类内容
     *
     * @creater bsw
     * @date 2017-08-28
     */
    public void put(String tableName,String familyName,String rowKey,T t){
        //通过execute方法进行put
        hbaseTemplate.execute(tableName, (table) -> {
            PutExtension putExtension = new PutExtension(familyName, rowKey.getBytes());
            Class clz = t.getClass();
            Field[] fields = clz.getDeclaredFields();
            //反射方式获取 PO对象的属性名称以及对应的值Value设置给列簇
            for (Field field: fields){
                String fieldName = field.getName();
                Object fieldValue = null;
                //根据属性名称拼装下 get方法
                String methodName = StringUtil.getFieldMethodNameByType("get",fieldName);

                Method getMethod = clz.getMethod(methodName);
                fieldValue = getMethod.invoke(t);
                //不为空才会设置到列簇
                if(!StringUtils.isEmpty(fieldValue)){
                    putExtension.build(fieldName,fieldValue);
                }

            }

            table.put(putExtension);
            return true;
        });
    }




    /**
     *  按照 表名称 列簇名称，主键key 查询单条数据明细
     *
     * @param t_name 表名称
     * @param rowKey 主键ID
     * @param familyName 列簇名称
     * @param clz 返回的Java实体类
     *              通常对应于列簇内所有column的Java实体
     *
     * @return  Object  对应于列簇内所有column的Java实体对象
     */
    public T  getObjectByFamily(String t_name,String rowKey,String familyName,Class<T> clz) {
        return (T) hbaseTemplate.get(t_name, rowKey, familyName,
                (result, i) -> new HBaseResultBuilder(familyName, result, clz).build(familyName).fetch());
    }

    /**
     * 按 表名称 列簇名称 起始时间 查询列表数据
     * @param t_name
     * @param familyName
     * @param clz
     * @return
     */
    public List<T> getObjectByFamilyAndPage(String t_name, String familyName,Scan scan,Class<T> clz){

        List<T> list = hbaseTemplate.find(t_name, scan,new RowMapper<T>(){
            public T mapRow(Result result, int rowNum) throws Exception {
                T t = clz.newInstance();
                List<Cell> ceList =   result.listCells();
                JSONObject jsonObj = new JSONObject();
                String  row = "";
                if(ceList!=null&&ceList.size()>0){
                    for(Cell cell:ceList){
                        row =Bytes.toString( cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
                        String value =Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
//                        String family =  Bytes.toString(cell.getFamilyArray(),cell.getFamilyOffset(),cell.getFamilyLength());
                        String name = Bytes.toString( cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
                        jsonObj.put(name, value);
                    }
                    t = JSON.parseObject(jsonObj.toString(),clz);
                }

                return  t;
            }
        });

        return list;
    }

    /**
     * @desc  按照rowKey 删除指定表指定列簇数据
     * @author BSW
     * @create 2017-09-09
     * @param t_name
     * @param rowName
     * @param familyName
     */
    public void deleteRow(String t_name,String rowName,String familyName){
        hbaseTemplate.delete(t_name,rowName,familyName);
    }


    /**
     * 创建表
     *    hbase基于列簇的存储结构特点
     *    列簇可以水平扩展，故只创建表名称和列簇名称即可
     *    列簇个数不能超过三个
     * @param t_Name
     * @param familyNames
     *
     * @date 2017-08-28
     */
    public void createTable(String t_Name,String ... familyNames){
        try {

            Configuration configuration = hbaseTemplate.getConfiguration();
            HbaseTemplate ht = new HbaseTemplate();
            ht.setConfiguration(configuration);

            Connection connection = ConnectionFactory.createConnection(configuration);
            //管理员对象
            Admin admin = connection.getAdmin();
            //表名
            TableName tableName = TableName.valueOf(t_Name);
            //检查表是否存在不存在才去创建
            if(admin.tableExists(tableName)){
                log.info("create table "+ t_Name+" fail. becouse table already exists");
                return;
            }
            //表描述
            HTableDescriptor desc = new HTableDescriptor(tableName);
            //判断如果familyNames 长度超过3个则不予创建
            if (familyNames != null && familyNames.length > 0 && familyNames.length <= 3){
                for (String name: familyNames) {
                    //列族描述
                    HColumnDescriptor coldef = new HColumnDescriptor(Bytes.toBytes(name));
                    //表加入列族
                    desc.addFamily(coldef);
                }
            }else{
                log.info("familyName length must be less than 4");
                return;
            }
            //创建表
            admin.createTable(desc);
            //校验表是否可用
            boolean avail = admin.isTableAvailable(tableName);
            if(avail){
                log.info("create table : "+ tableName.getNameAsString() + "success");
            }else{
                log.info("create table : "+ tableName.getNameAsString() + "fail");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

