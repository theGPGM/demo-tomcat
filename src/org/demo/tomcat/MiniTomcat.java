package org.demo.tomcat;

import cn.hutool.core.util.NetUtil;
import org.demo.http.Request;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 最简单的 web 服务器
 */
public class MiniTomcat {
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
                System.out.println("浏览器的输入信息： \r\n" + request.getRequest());
                System.out.println("uri:" + request.getUri());
                //响应客户端请求
                OutputStream os = socket.getOutputStream();
                String responseHead = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";
                String responseContent = "Thanks for request";
                String response = responseHead + responseContent;
                os.write(response.getBytes());
                os.flush();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}