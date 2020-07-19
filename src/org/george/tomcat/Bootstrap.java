package org.george.tomcat;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import org.george.http.Constant;
import org.george.http.ResponseHead;
import org.george.http.Request;
import org.george.http.Response;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 最简单的 web 服务器
 */
public class Bootstrap {
    public static final int port = 10001;

    public static void main(String[] args) {
        try {
            if (!NetUtil.isUsableLocalPort(port)) {
                throw new IllegalArgumentException(port + "端口被占用");
            }
            ServerSocket serverSocket = new ServerSocket(port);
            // 接收客户端请求
            while (true) {
                Socket socket = serverSocket.accept();
                //封装客户端请求
                Request request = new Request(socket);
                //输出信息
                System.out.println("浏览器的输入信息： \r\n" + request.getRequest());
                System.out.println("uri:" + request.getUri());
                //响应客户端请求
                Response response = new Response();
                String uri = request.getUri();
                //请求不符合规范
                if(uri == null) continue;
                    if("/".equals(uri)){
                        String html = "Hello DemoTomcat!";
                        response.getWriter().println(html);
                    }else{
                        String fileName = StrUtil.removePrefix(uri, "/");
                        File file = FileUtil.file(Constant.rootFolder, fileName);
                        if(file.exists()){
                            String content = FileUtil.readUtf8String(file);
                            response.getWriter().println(content);
                        }else{
                            response.getWriter().println("404 File Not Found");
                        }
                }
                send(socket, response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void send(Socket socket, Response response) throws IOException {
        OutputStream os = socket.getOutputStream();
        try{
            //设置响应头
            String contentType = response.getContentType();
            String responseHead = ResponseHead.RESPONSE_STATUS_200.getResponse();
            responseHead = StrUtil.format(responseHead, contentType);
            byte[] head = responseHead.getBytes();
            //获取响应体
            byte[] content = response.getBody();
            byte[] responseBytes = new byte[head.length + content.length];
            ArrayUtil.copy(head,0, responseBytes, 0, head.length);
            ArrayUtil.copy(content, 0, responseBytes, head.length, content.length);
            //传输
            os.write(responseBytes);
            os.flush();
        }finally {
            os.close();
        }
    }
}