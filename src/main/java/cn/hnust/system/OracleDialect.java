package cn.hnust.system;

public class OracleDialect extends Dialect {

    /* (non-Javadoc)
     * @see org.mybatis.extend.interceptor.IDialect#getLimitString(java.lang.String, int, int)
     */
    @Override
    public String getLimitString(String sql, int offset, int limit) {
        if (0 == offset && Integer.MAX_VALUE == limit) {
            return sql;
        }
        sql = sql.trim();
        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);

        pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");

        pagingSelect.append(sql);

        pagingSelect.append(" ) row_ ) where rownum_ > ").append(offset).append(" and rownum_ <= ").append(offset + limit);

        return pagingSelect.toString();
    }

}  