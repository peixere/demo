package cn.gotom.util;


import java.util.ArrayList;
import java.util.List;

import cn.gotom.annotation.Description;

/**
 *
 * @author peixere@qq.com 分页
 * @version 1.0
 *
 */
public class Pagination<T> {

    @Description("页号")
    private int index = 0;
    @Description("每页行数")
    private int rowSize = 25;
    @Description("总行数")
    private int rowCount;
    @Description("当前页数据")
    protected List<T> list;

    @Description("分页类")
    public Pagination() {
        list = new ArrayList<T>();
    }

    public Pagination(int rowCount, List<T> list, int rowSize, int index) {
        this.rowCount = rowCount;
        this.rowSize = rowSize;
        this.list = list;
        this.index = index;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int pageIndex) {
        this.index = pageIndex;
    }

    public int getRowSize() {
        return rowSize;
    }

    public void setRowSize(int rowSize) {
        this.rowSize = rowSize;
    }

    @Description("总页数")
    public int size() {
        int totalSize = rowCount / rowSize;
        return rowCount % rowSize > 0 ? totalSize + 1 : totalSize;
    }

    @Description("有下一页")
    public boolean next() {
        return (index + 1) < size();
    }

    @Description("有上一页")
    public boolean prev() {
        return index > 0;
    }
}
