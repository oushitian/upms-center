package com.jolly.upms.manager.vo;

import java.util.List;

/**
 * 分页器
 * Created by djb
 * on 2014/11/18.
 */
public class Pagination<T> {

    private List<T> result; //对象记录结果集
    private int totalCount = 0; // 总记录数
    private int pageSize = 20; // 每页显示记录数
    private int totalPage = 1; // 总页数
    private int pageNum = 1; // 当前页

    public Pagination() {
        init(this.totalCount, this.pageSize, this.pageNum);
    }

    public Pagination(List<T> result) {
        this.result = result;
        init(this.totalCount, this.pageSize, this.pageNum);
    }

    public Pagination(List<T> result, int totalCount) {
        this.result = result;
        this.totalCount = totalCount;
        init(this.totalCount, this.pageSize, this.pageNum);
    }

    public Pagination(List<T> result, int totalCount, int pageSize) {
        this.result = result;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        init(this.totalCount, this.pageSize, this.pageNum);
    }

    private void init(int totalCount, int pageSize, int pageNum) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.totalPage = (this.totalCount - 1) / this.pageSize + 1;

        if (pageNum < 1) {
            this.pageNum = 1;
        } else if (pageNum > this.totalPage) {
            this.pageNum = this.totalPage;
        } else {
            this.pageNum = pageNum;
        }
    }


    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
