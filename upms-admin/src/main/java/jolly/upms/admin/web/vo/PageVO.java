package jolly.upms.admin.web.vo;

/**
 * 分页参数封装
 *
 * @author chenjunchi
 * @since 2017-03-21
 */
public class PageVO {

    /**
     * 分页大小
     */
    private int rows = 20;

    /**
     * 当前页，从1开始
     */
    private int page = 1;

    /**
     * 计算分页查询时的start位置
     *
     * @return
     */
    public int getStart() {
        page = page < 1 ? 1 : page;
        return (page - 1) * rows;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
