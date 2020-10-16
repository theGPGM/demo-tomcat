package org.george.test;

import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import junit.framework.Assert;
import org.george.util.UtilBrowser;
import org.junit.BeforeClass;
import org.junit.Test;

public class TomcatTest {
    private static final int port = 10001;
    private static final String ip = "localhost";

    /**
     * 利用端口是否被占用，检测 tomcat 是否启动
     */
    @BeforeClass
    public static void prepare(){
        if(NetUtil.isUsableLocalPort(port)){
            System.out.println("tomcat 还未启动，请启动 tomcat");
            return;
        }else{
            System.out.println("tomcat 启动");
        }
    }

    @Test
    public void test(){
        String html = getResponseHtml("/app/index.html");
        Assert.assertEquals("Thanks for request", html);
    }

    /**
     * 获取服务器传来的 html 网页
     * @param uri
     * @return
     */
    private String getResponseHtml(String uri){
        String url = StrUtil.format("http://{}:{}{}", ip, port, uri);
        return UtilBrowser.getContentString(url);
    }
}
