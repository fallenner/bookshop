package cn.hnust.system;  
  
public abstract class Dialect {  
  
    public static enum Type{  
        MYSQL,  
        ORACLE,
        DB2,
        SQLSERVER
    }  
      
    public abstract String getLimitString(String sql, int skipResults, int maxResults);  
      
}  