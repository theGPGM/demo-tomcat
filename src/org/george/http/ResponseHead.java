package org.george.http;

/**
 * 封装响应头消息的类
 */
public enum ResponseHead {
    RESPONSE_STATUS_200("HTTP/1.1 200 OK\r\nContent-Type: {}\r\n\r\n")
    ;

    private String response;

    private ResponseHead(String response){
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
