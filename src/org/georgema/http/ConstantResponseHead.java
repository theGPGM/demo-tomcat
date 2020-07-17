package org.georgema.http;

/**
 * 封装响应头消息的类
 */
public enum ConstantResponseHead {
    RESPONSE_STATUS_200("HTTP/1.1 200 OK\r\nContent-Type: {}\r\n\r\n")
    ;

    private String response;

    private ConstantResponseHead(String response){
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
