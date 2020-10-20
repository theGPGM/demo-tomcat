package org.george.http;

import org.george.exception.EngineException;
import org.george.util.ServerXMLUtil;

import java.io.File;
import java.util.List;

public class Engine {

    private List<Host> hosts;

    private String defaultHostName;

    public Engine(){
        this.hosts = ServerXMLUtil.getXmlHosts();
        this.defaultHostName = ServerXMLUtil.getEngineDefaultHost();

        /**
         * 将项目下的应用加入 defaultHost 中
         */
        Host defaultHost = defaultHost();
        scanContextsOnWebapps(defaultHost);

        if(this.defaultHostName == null){
            throw new EngineException(Constant.EngineExceptionMessage.ENGINE_DEFAULT_HOST_NOT_EXISTS);
        }
    }

    public Host defaultHost(){
        for(Host host : hosts){
            if(host.getHostName().equals(this.defaultHostName)){
                return host;
            }
        }
        throw new EngineException(Constant.EngineExceptionMessage.ENGINE_DEFAULT_HOST_NOT_EXISTS);
    }

    private void scanContextsOnWebapps(Host host){
        File[] files = Constant.Folder.WEBAPPS_FOLDER.listFiles();
        for(File file : files){
            if(file.isDirectory()){
                wrapContext(file, host);
            }
        }
    }

    /**
     * 将 path 与 Context 映射在一起
     * @param file
     */
    private void wrapContext(File file, Host host){
        String path = file.getName();
        if("root".equals(path)){
            path = "/";
        }else{
            path = "/" + path;
        }
        String docPath = file.getAbsolutePath();
        Context context = new Context(path, docPath);
        host.addContext(context);
    }

    @Override
    public String toString() {
        return "Engine{" +
                "hosts=" + hosts +
                '}';
    }
}
