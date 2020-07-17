package org.georgema.tomcat;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import org.georgema.http.ConstantResponseHead;
import org.georgema.http.Request;
import org.georgema.http.Response;

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
                Response response = new Response();
                String content = "Thanks for request";
                response.getWriter().println(content);
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
            String responseHead =ConstantResponseHead.RESPONSE_STATUS_200.getResponse();
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