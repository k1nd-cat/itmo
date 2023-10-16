package net.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class Response implements Serializable {

    public static final int RESULT_OK = 0;
    public static final int RESULT_ERROR = 1;

    @Getter
    @Setter
    private int code;

    @Setter
    private Object body;

    public static Response ok() {
        return new Response(RESULT_OK);
    }

    public static Response error() {
        return new Response(RESULT_ERROR);
    }

    public static Response error(String body) {
        Response response = new Response(RESULT_ERROR);
        response.setBody(body);
        return response;
    }

    public Response() { }

    public Response(int code) {
        this.code = code;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBody(Class<T> type) {
        return (T) body;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", body=" + body +
                '}';
    }
}
