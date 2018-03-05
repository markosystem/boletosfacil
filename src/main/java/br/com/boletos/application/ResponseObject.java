package br.com.boletos.application;

public class ResponseObject {
    private Integer status;
    private String message;
    private Object data;

    public ResponseObject() {

    }

    public ResponseObject(Integer status, Object data) {
        this.status = status;
        this.data = data;
    }

    public ResponseObject(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
