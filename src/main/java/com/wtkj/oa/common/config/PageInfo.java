package com.wtkj.oa.common.config;

import com.github.pagehelper.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageInfo<E> implements Serializable {
    private static final long serialVersionUID = -5596707890459364028L;

    private long totalRows;

    private int totalPage;

    private int pageSize;

    private int pageNum;

    private int nextPages;

    private List<E> result;

    public PageInfo() {
        super();
    }

    public PageInfo(int pageNum, int pageSize, List<E> result) {
        super();
        if (result instanceof Page) {
            Page page = (Page)result;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.totalPage = page.getPages();
            this.result = page;
            this.totalRows = page.getTotal();
        } else {
            pageNum = pageNum == 0 ? 1 : pageNum;
            pageSize = pageSize == 0 ? 10 : pageSize;
            this.pageNum = pageNum;
            this.pageSize = pageSize;
            this.totalRows = result.size();
            this.totalPage = (int) (totalRows%pageSize == 0 ? totalRows/pageSize : totalRows/pageSize + 1);
            if((pageNum - 1) * pageSize >= result.size()){
                this.result = new ArrayList<>();
            } else if(pageNum * pageSize >= result.size()) {
                this.result = result.subList((pageNum - 1) * pageSize, result.size());
            } else {
                this.result = result.subList((pageNum - 1) * pageSize, pageNum * pageSize);
            }
        }
    }

    public PageInfo(int totalRows, int pageSize, int pageNum, int nextPages, List<E> result) {
        pageNum = pageNum == 0 ? 1 : pageNum;
        pageSize = pageSize == 0 ? 10 : pageSize;
        this.totalRows = totalRows;
        this.totalPage = totalRows%pageSize == 0 ? totalRows/pageSize : totalRows/pageSize + 1;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.nextPages = nextPages;
        this.result = result;
    }

    public long getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(long totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getNextPages() {
        return nextPages;
    }

    public void setNextPages(int nextPages) {
        this.nextPages = nextPages;
    }

    public List<E> getResult() {
        return result;
    }

    public void setResult(List<E> result) {
        this.result = result;
    }
}
