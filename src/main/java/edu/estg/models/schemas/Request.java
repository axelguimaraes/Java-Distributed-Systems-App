package edu.estg.models.schemas;

public class Request<T> {
    private RequestType type;
    private T data;

    public Request(RequestType type) {
        this.type = type;
    }

    public Request(RequestType type, T data) {
        this.type = type;
        this.data = data;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
