package org.george.http;

import cn.hutool.core.util.StrUtil;
import org.george.util.UtilBrowser;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Request {

    private String request;
    private String uri;
    private Socket socket;
    public Request(Socket socket) throws IOException {
        this.socket = socket;
        parseHttpRequest();
        if(StrUtil.isEmpty(request))
            return;
        parseUri();
    }

    /**
     * 将客户端的请求解析为字符串
     * @throws IOException
     */
    private void parseHttpRequest() throws IOException {
        InputStream is = this.socket.getInputStream();
        byte[] bytes = UtilBrowser.readBytes(is);
        request = new String(bytes, "utf-8");
    }

    /**
     * 解析客户端请求的 uri
     */
    private void parseUri() {
        String temp;
        temp = StrUtil.subBetween(request, " ", " ");
        //无参
        if (!StrUtil.contains(temp, '?')) {
            uri = temp;
            return;
        }
        //带参请求，去除 '?' 后的字符串
        temp = StrUtil.subBefore(temp, '?', false);
        uri = temp;
    }

    public String getUri() {
        return uri;
    }

    public String getRequest(){
        return request;
    }
}