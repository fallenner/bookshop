package cn.hnust.system;

/**
 * Created by dzq on 2016-2-15.
 */
public class MysqlDialect extends Dialect {
    @Override
    public String getLimitString(String sql, int offset, int limit) {
        if (0 == offset && Integer.MAX_VALUE == limit) {
            return sql;
        }
        sql = sql.trim();
        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
        pagingSelect.append(sql);
        pagingSelect.append(" limit ");
        pagingSelect.append(offset);
        pagingSelect.append(",");
        pagingSelect.append(limit);
        return pagingSelect.toString();
    }
}
