package cn.hnust.bookshop.bean;

public class ResponseResult<T> {
    private Integer code;
    private String msg;
    private T result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.code = 0;
        this.result = result;
    }

    public void setErrorMsg(String msg) {
        this.code = -1;
        this.msg = msg;
    }

    public void setSuccessMsg(String msg) {
        this.code = 0;
        this.msg = msg;
    }

    public void setUnAuthMsg() {
        this.code = 101;
        this.msg = "当前用户未登录";
    }
}
