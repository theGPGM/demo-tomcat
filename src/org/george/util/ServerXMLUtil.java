package org.george.util;

import cn.hutool.core.io.FileUtil;
import org.george.exception.XMLParseException;
import org.george.http.Constant;
import org.george.http.Context;
import org.george.http.Host;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ServerXMLUtil {

    private static Document xmlDoc;

    static{
        String xml = FileUtil.readUtf8String(Constant.Folder.SEVER_XML_FILE);
        xmlDoc = Jsoup.parse(xml);
        if(xmlDoc == null){
            throw new XMLParseException(Constant.XMLParseExceptionMessage.XML_FILE_NOT_FOUND );
        }
    }

    public static void loadXmlContexts(Host host, Element e){

        List<Context> list = new ArrayList<>();

        for(Element element : e.select("Context")){
            String path = element.attr("path");
            String docPath = element.attr("docPath");
            if(path == null || docPath == null){
                throw new XMLParseException(Constant.XMLParseExceptionMessage.XML_ATTR_ERROR);
            }
            if(!path.startsWith("/")){
                throw new XMLParseException(Constant.XMLParseExceptionMessage.CONTEXT_PATH_FORMAT_ERROR);
            }

            if(!new File(docPath).exists()){
                throw new XMLParseException(Constant.XMLParseExceptionMessage.CONTEXT_DOC_NOT_EXISTS);
            }
            Context context = new Context(path, docPath);
            host.addContext(context);
        }
    }

    public static String getHostName(){
        Element host = xmlDoc.select("Host").first();

        if(host == null){
            throw new XMLParseException(Constant.XMLParseExceptionMessage.HOST_NOT_FOUND);
        }

        String name = host.attr("name");
        if(name == null || name.length() <= 0){
            throw new XMLParseException(Constant.XMLParseExceptionMessage.HOST_NAME_NOT_FOUND);
        }
        return name;
    }

    /**
     * 找到 server.xml 中的
     * <Engine defaultHost="xxx">
     * </Engine>
     * 的 defaultHost
     * @return
     */
    public static String getEngineDefaultHost(){
        Element engine = xmlDoc.select("Engine").first();

        if(engine == null){
            throw new XMLParseException(Constant.XMLParseExceptionMessage.ENGINE_NOT_FOUND);
        }

        String host = engine.attr("defaultHost");
        if(host == null || host.length() <= 0){
            throw new XMLParseException(Constant.XMLParseExceptionMessage.ENGINE_DEFAULT_HOST_NOT_FOUND);
        }

        return host;
    }

    public static List<Host> getXmlHosts(){
        Element element = xmlDoc.selectFirst("Engine");
        if(element == null){
            throw new XMLParseException(Constant.XMLParseExceptionMessage.ENGINE_NOT_FOUND);
        }

        List<Host> list = new ArrayList<>();
        Elements hosts = element.select("Host");
        for(Element e : hosts){
            String hostName = e.attr("name");
            Host host = new Host(hostName);

            // 加载 host 中的 context
            loadXmlContexts(host, e);
            list.add(host);
        }
        return list;
    }

    public String getServiceName(){
        Element element = xmlDoc.selectFirst("Service");
        if(element == null) {
            throw new XMLParseException(Constant.XMLParseExceptionMessage.SERVICE_TAG_NOT_FOUND);
        }

        String name = element.attr("name");
        if(name == null || name.length() <= 0){
            throw new XMLParseException(Constant.XMLParseExceptionMessage.SERVICE_NAME_NOT_FOUND);
        }

        return name;
    }
}
