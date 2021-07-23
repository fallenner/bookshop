package cn.hnust.system;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.Properties;
import java.util.regex.Pattern;

@Intercepts(@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class}))
public class PaginationInterceptor implements Interceptor {
    private Logger log = Logger.getLogger(PaginationInterceptor.class);
    private final static String SQL_SELECT_REGEX = "(?is)^\\s*SELECT.*$";
    private final static String SQL_COUNT_REGEX = "(?is)^\\s*SELECT\\s+COUNT\\s*\\(\\s*(?:\\*|\\w+)\\s*\\).*$";
    private String dialect;

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    // @Override
    public Object intercept(Invocation inv) throws Throwable {

        StatementHandler target = (StatementHandler) inv.getTarget();
        Dialect.Type databaseType = null;
        try {
            databaseType = Dialect.Type.valueOf(dialect.toUpperCase());
        } catch (Exception e) {
            //ignore
        }
        if (databaseType == null) {
            throw new RuntimeException("the value of the dialect property in xml is not defined");
        }
        Dialect dialect = null;
        switch (databaseType) {
            case ORACLE:
                dialect = new OracleDialect();
                break;
            case MYSQL://需要实现MySQL的分页逻辑
                dialect = new MysqlDialect();
                break;
            default:
                break;
        }
        BoundSql boundSql = target.getBoundSql();
        String sql = boundSql.getSql();
        if (StringUtils.isBlank(sql)) {
            return inv.proceed();
        }
        // 只有为select查询语句时才进行下一步
        if (sql.matches(SQL_SELECT_REGEX)
                && !Pattern.matches(SQL_COUNT_REGEX, sql)) {
            Object obj = FieldUtils.readField(target, "delegate", true);
            // 反射获取 RowBounds 对象。
            RowBounds rowBounds = (RowBounds) FieldUtils.readField(obj,
                    "rowBounds", true);
            // 分页参数存在且不为默认值时进行分页SQL构造
            if (rowBounds != null && rowBounds != RowBounds.DEFAULT) {
//				FieldUtils.writeField(boundSql, "sql", newSql(sql, rowBounds),
//						true);
                FieldUtils.writeField(boundSql, "sql", dialect.getLimitString(sql, rowBounds.getOffset(), rowBounds.getLimit()), true);
                log.debug("sql>>>>>" + boundSql.getSql().replaceAll("\n", ""));
                // 一定要还原否则将无法得到下一组数据(第一次的数据被缓存了)
                FieldUtils.writeField(rowBounds, "offset",
                        RowBounds.NO_ROW_OFFSET, true);
                FieldUtils.writeField(rowBounds, "limit",
                        RowBounds.NO_ROW_LIMIT, true);
            }
        }
        return inv.proceed();
    }

	/*public String newSql(String oldSql, RowBounds rowBounds) {

		String start = " SELECT * FROM   (SELECT   row_.*, ROWNUM rownum_ FROM ( ";
		String end = " ) row_ WHERE   ROWNUM <= " + rowBounds.getLimit() + "+"
				+ rowBounds.getOffset() + ") WHERE  rownum_ > "
				+ rowBounds.getOffset();
		return start + oldSql + end;
	}*/

    // @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    // @Override
    public void setProperties(Properties arg0) {
        System.out.println(arg0);
    }
}