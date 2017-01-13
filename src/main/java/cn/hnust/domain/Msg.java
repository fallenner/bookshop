package cn.hnust.domain;

public class Msg {
    //回传状态 true 成功， false 失败
    private String msgStatus;
    //消息提示文本
    private String msgText;

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public void setSuccessMsg(String text){
        this.msgStatus = "true";
        this.msgText = text;
    }

    public void setErrorMsg(String text){
        this.msgStatus = "false";
        this.msgText = text;
    }

}
