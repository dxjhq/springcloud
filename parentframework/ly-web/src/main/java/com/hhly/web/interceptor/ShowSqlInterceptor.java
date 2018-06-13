package com.hhly.web.interceptor;


/**
import com.alibaba.druid.sql.SQLUtils;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.StatementInterceptor;

import java.sql.SQLException;
import java.util.Properties;

 @Deprecated
 //打印 MariaDB 的执行语句(基于 mysql 驱动的拦截器)
public class ShowSqlInterceptor implements StatementInterceptor {
    public void init(Connection connection, Properties properties) throws SQLException {}

    public ResultSetInternalMethods preProcess(String sql, Statement statement,
                                               Connection connection) throws SQLException {
        if (U.isBlank(sql) && statement != null) {
            sql = statement.toString();
            if (U.isNotBlank(sql) && sql.indexOf(':') > 0) {
                sql = SQLUtils.formatMySql(sql.substring(sql.indexOf(':') + 1).trim());
            }
        }
        if (U.isNotBlank(sql)) {
            if (LogUtil.SQL_LOG.isDebugEnabled()) {
                LogUtil.SQL_LOG.debug(sql);
            }
        }
        return null;
    }

    public ResultSetInternalMethods postProcess(String s, Statement statement,
                 ResultSetInternalMethods resultSetInternalMethods, Connection connection) throws SQLException {
        return null;
    }

    public boolean executeTopLevelOnly() { return false; }
    public void destroy() {}
}
*/