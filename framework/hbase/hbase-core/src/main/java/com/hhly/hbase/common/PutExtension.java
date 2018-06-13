package com.hhly.hbase.common;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * 自定义put 扩展下，hbaseTempplate 对象提供的工具太SHI了。。。不能用。。
 *
 * @date 2017-08-28
 * @creater bsw
 */
public class PutExtension extends Put {

        String familyName = "behavior";

        public PutExtension(String familyName, byte[] row) {
            super(row);
            this.familyName = familyName;
        }

        public PutExtension build(String paramName, Object param) throws IOException {
            if (param != null) {
                this.addColumn(familyName.getBytes(), paramName.getBytes(),
                        Bytes.toBytes(param.toString()));
            }
            return this;
        }
}
