package org.george.tomcat;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import cn.hutool.system.SystemUtil;
import org.george.exception.ContextException;
import org.george.http.*;
import org.george.util.ServerXMLUtil;
import org.george.util.ThreadPoolUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * 最简单的 web 服务器
 */
public class Bootstrap {

    public static final int port = 10001;

    private static final String serverVersion = "0.01";


    /**
     * 程序运行的主要逻辑：在这里接收来自客户端传输的 socket，解析其中的请求内容，并返回相应的响应结果
     * @param args
     */
    public static void main(String[] args) {
        try {
            startLog();

            Engine engine = new Engine();

            ServerSocket serverSocket = new ServerSocket(port);

            // 接收客户端请求
            while (true) {
                Socket socket = serverSocket.accept();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Request request = new Request(socket, engine);
                            Response response = new Response();
                            // 获取请求的应用信息封装类：应用的地址信息
                            Context context = request.getContext();
                            // 获取请求的地址
                            String uri = request.getUri();
                            if (uri == null) return;
                            if ("/".equals(uri)) {
                                response.getWriter().println("welcome to mini tomcat!");
                            } else {
                                String[] split = uri.split("/");
                                String filename = split[split.length - 1];
                                File file = FileUtil.file(context.getDocPath(), filename);
                                if (file.exists()) {
                                    String content = FileUtil.readUtf8String(file);
                                    response.getWriter().println(content);
                                } else {
                                    response.getWriter().println(Constant.ResponseMessage.FILE_NO_FOUND);
                                }
                            }
                            send(socket, response);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                };
                ThreadPoolUtil.run(runnable);
            }
        } catch (IOException e) {
            LogFactory.get().error(e);
            e.printStackTrace();
        }
    }

    /**
     * 将封装在 response 中的信息发送出去
     *
     * @param socket
     * @param response
     * @throws IOException
     */
    private static void send(Socket socket, Response response) throws IOException {
        OutputStream os = socket.getOutputStream();
        try {
            //设置响应头
            String contentType = response.getContentType();
            String responseHead = ResponseHead.RESPONSE_STATUS_200.getResponse();
            responseHead = StrUtil.format(responseHead, contentType);
            byte[] head = responseHead.getBytes();
            //获取响应体
            byte[] content = response.getBody();

            // 将响应头和响应体结合在一起
            byte[] responseBytes = new byte[head.length + content.length];
            ArrayUtil.copy(head, 0, responseBytes, 0, head.length);
            ArrayUtil.copy(content, 0, responseBytes, head.length, content.length);

            //传输到对应的设备中
            os.write(responseBytes);
            os.flush();
        } finally {
            os.close();
        }
    }

    public static void startLog() {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("Server version", "Mini Tomcat/" + serverVersion);
        info.put("Server built", "\t" + new Date().toString());
        info.put("Server number", "\t" + serverVersion);
        info.put("OS name", "\t\t" + SystemUtil.get("os.name"));
        info.put("OS version", "\t" + SystemUtil.get("os.version"));
        info.put("Architecture", "\t" + SystemUtil.get("os.arch"));
        info.put("JVM version", "\t" + SystemUtil.get("java.runtime.version"));
        info.put("JVM vendor", "\t" + SystemUtil.get("java.vm.specification.vendor"));

        Set<String> keys = info.keySet();
        for (String key : keys)
            LogFactory.get().info(key + ":\t\t" + info.get(key));
    }
}