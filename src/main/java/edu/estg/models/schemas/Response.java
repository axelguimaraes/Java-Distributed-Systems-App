package edu.estg.models.schemas;

public class Response<T> {
    private ResponseStatus status;
    private RequestType type;
    private String message;
    private T data;

    public Response(ResponseStatus status, RequestType type, String message, T data) {
        this.status = status;
        this.type = type;
        this.message = message;
        this.data = data;
    }

    public Response(ResponseStatus status, RequestType type, T data) {
        this(status, type, null, data);
    }

    public Response(ResponseStatus status, RequestType type, String message) {
        this(status, type, message, null);
    }

    public Response(ResponseStatus status, RequestType type) {
        this(status, type, null, null);
    }

    public Response(ResponseStatus status, String message) {
        this(status, null, message, null);
    }

    public Response(RequestType type) {
        this(null, type, null, null);
    }

    public Response() {
        this(null, null, null, null);
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
