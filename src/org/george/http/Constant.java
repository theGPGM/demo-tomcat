package org.george.http;

import cn.hutool.system.SystemUtil;

import java.io.File;

public class Constant {

    public static class Folder{
        public final static File WEBAPPS_FOLDER = new File(SystemUtil.get("user.dir"), "webapps");

        public static final File CONF_FOLDER = new File(SystemUtil.get("user.dir"),"conf");

        public static final File SEVER_XML_FILE = new File(CONF_FOLDER, "server.xml");
    }

    public static class Context{
        public final static String ROOT_CONTEXT_PATH = "/";
    }

    public static class ThreadPoolParameter{
        public final static Integer MIN_POOL_SIZE = 20;

        public final static Integer MAX_POOL_SIZE = 100;

        public final static Long MAX_WAITING_TIME = 60l;

        public final static Integer BLOCKING_QUEUE_CAPACITY = 10;
    }

    public static class ResponseMessage {
        public static final String FILE_NO_FOUND = "404, the file not found!";
    }

    public static class XMLParseExceptionMessage{
        public static final String XML_ATTR_ERROR = "can't find the path or docPath, check again";

        public static final String CONTEXT_PATH_FORMAT_ERROR = "this context path format is illegal, please check, must like this: \\app";

        public static final String CONTEXT_DOC_NOT_EXISTS = "this context doc path can't found, please check";
    }

    public static class ContextExceptionMessage{

        public static final String CONTEXT_ALREADY_EXISTS = "this context {} already exists, please check";
    }
}
