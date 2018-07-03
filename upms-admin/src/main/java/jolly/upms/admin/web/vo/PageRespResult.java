package jolly.upms.admin.web.vo;

/**
 * 公共分页响应封装
 *
 * @author chenjunchi
 * @since 2017-03-21
 */
public class PageRespResult extends RespResult {

    /**
     * 记录总数
     */
    private int total;



    public PageRespResult() {
    }

    public PageRespResult(Object rows, int total) {
        super(rows);
        this.total = total;
    }

    public PageRespResult(int code, String msg) {
        super(code, msg);
    }

    public PageRespResult(int code, String msg, Object rows, int total) {
        super(code, msg, rows);
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


}
