package org.demo.tomcat;

import cn.hutool.core.util.NetUtil;
import com.sun.org.apache.bcel.internal.generic.ATHROW;
import jdk.internal.util.xml.impl.Input;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 最简单的一个 web 服务器
 */
public class Bootstrap {
    public static final int port = 10001;
    public static final int buffSize = 1024;
    public static void main(String[] args) {
        try {
            // 处理端口被占用
            if(!NetUtil.isUsableLocalPort(port)){
                    throw new IllegalArgumentException(port + "端口被占用");
                }
                ServerSocket serverSocket = new ServerSocket(port);
                while(true){
                    Socket socket = serverSocket.accept();
                    // 接收客户端的请求
                    InputStream is = socket.getInputStream();
                    byte[] buff = new byte[1024];
                is.read(buff);
                String request = new String(buff, "utf-8");
                System.out.println(request);
                // 响应客户端请求
                OutputStream os = socket.getOutputStream();
                String responseHead = "HTTP/1.1 200 OK\r\nContent-Type:text/html\r\n\r\n";
                String responseContent = "Thanks for your request";
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
