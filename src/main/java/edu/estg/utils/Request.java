package edu.estg.utils;

public class Request<T> {
    private RequestType type;
    private T data;

    public Request(RequestType type, T data) {
        this.type = type;
        this.data = data;
    }

    public Request(RequestType type) {
        this.type = type;
        this.data = null;
    }

    public RequestType getType() {
        return this.type;
    }

    public T getData() {
        return this.data;
    }

}
