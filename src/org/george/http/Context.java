package org.george.http;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.log.LogFactory;

public class Context {

    /**
     * 应用所在逻辑路径
     */
    private String path;

    /**
     * 应用所在绝对路径
     */
    private String docPath;

    private TimeInterval timeInterval = DateUtil.timer();

    public Context(String path, String docPath){
        this.path = path;
        this.docPath = docPath;
        LogFactory.get().info("Deploying web app in {}", this.docPath);
        LogFactory.get().info("Deployment of web app directory has finished in {} ms", timeInterval.intervalMs());
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }
}
