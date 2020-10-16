package org.george.util;

import cn.hutool.core.io.FileUtil;
import org.apache.tomcat.util.bcel.Const;
import org.george.exception.XMLParseException;
import org.george.http.Constant;
import org.george.http.Context;
import org.george.tomcat.Bootstrap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.List;

public class ServerXMLUtil {

    public static void loadXmlContexts(){
        String xml = FileUtil.readUtf8String(Constant.Folder.SEVER_XML_FILE);

        if(xml == null){
            return;
        }

        Document document = Jsoup.parse(xml);
        if(document == null){
            return;
        }

        Elements elements = document.select("Context");
        for(Element element : elements){
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
            Bootstrap.addContext(context);
        }
    }
}
