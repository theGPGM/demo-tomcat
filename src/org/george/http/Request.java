package org.george.http;

import cn.hutool.core.util.StrUtil;
import org.apache.tomcat.util.bcel.Const;
import org.george.tomcat.Bootstrap;
import org.george.util.UtilBrowser;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Request {

    private String request;

    private String uri;

    private Socket socket;

    private Context context;

    /**
     * 将客户端发来的 socket 封装成 Request 对象
     * @param socket
     * @throws IOException
     */
    public Request(Socket socket) throws IOException {
        this.socket = socket;
        parseHttpRequest();
        if(StrUtil.isEmpty(request))
            return;
        parseContextPath();
        parseUri();
    }

    /**
     * 将客户端的请求解析为字符串
     * @throws IOException
     */
    private void parseHttpRequest() throws IOException {
        InputStream is = this.socket.getInputStream();
        byte[] bytes = UtilBrowser.readBytes(is);
        request = new String(bytes, "UTF-8");
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

    /**
     * 解析请求所属的应用
     * @return
     */
    public void parseContextPath(){
        String contextPath = StrUtil.subBetween(request, "/", "/");
        if(contextPath == null){
            contextPath = "/";
        }else{
            contextPath = "/" + contextPath;
        }

        Context context = Bootstrap.getContext(contextPath);
        if(context == null){
            context = Bootstrap.getContext(Constant.Context.ROOT_CONTEXT_PATH);
        }
        setContext(context);
    }

    public String getUri() {
        return uri;
    }

    public String getRequest(){
        return request;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}