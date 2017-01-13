package cn.hnust.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pager implements Serializable {
    private static final long serialVersionUID = 8848523495013555357L;
    public static int DEFAULT_PAGE_SIZE = 20;
    public static int MAX_FETCH_SIZE = 200;
    private int pageSize;
    private int page = 1;
    private int total;
    private int records;
    private List<?> rows = new ArrayList<>();

    public Pager() {
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    public Pager(int page, int pageSize) {
        this.pageSize = pageSize;
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        if (this.total < 0) {
            this.total = (int) Math.ceil((double) this.records / (double) this.pageSize);
        }
        return total;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
        this.records = records > 0 ? records : 0;
        this.total = (int) Math.ceil((double) records / (double) this.pageSize);
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
