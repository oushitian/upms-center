package jolly.upms.admin.web.vo;

/**
 * 公共响应封装
 *
 * @author chenjunchi
 * @since 2017-03-21
 */
public class RespResult {

    public enum RespCode {

        PWD_PROMPT(1, "密码提示"),
        SUCCESS(0, "操作成功"),
        SYS_EXCEPTION(-1, "系统异常"),
        NO_PRIVILEGE(-2, "没有权限"),
        PARAM_EXCEPTION(-3, "参数异常"),
        APP_NAME_DUPLICATE(-11, "应用系统名称重复"),
        APP_KEY_DUPLICATE(-12, "应用系统Key重复"),
        NO_LOGIN(-13, "没有登录"),
        NO_ENTITY(-14, "查询不到对象");

        private final int code;

        private final String msg;

        RespCode(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    /**
     * 响应码
     */
    private int code = RespCode.SUCCESS.getCode();

    /**
     * 响应码描述
     */
    private String msg = RespCode.SUCCESS.getMsg();

    /**
     * 具体数据
     */
    private Object rows;

    public RespResult() {
    }

    public RespResult(Object rows) {
        this.rows = rows;
    }

    public RespResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public RespResult(int code, String msg, Object rows) {
        this.code = code;
        this.msg = msg;
        this.rows = rows;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }
}
