package org.george.http;

import cn.hutool.core.util.StrUtil;
import org.george.exception.ContextException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Host {

    /**
     * 存放路径与应用映射的集合
     */
    private Map<String, Context> contextMap;

    private String hostName;

    public Host(String hostName) {
        this.contextMap = new HashMap<>();
        this.hostName = hostName;
    }

    public Context getContext(String path){
        return contextMap.get(path);
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void addContext(Context context){
        if(contextMap.get(context.getPath()) != null){
            throw new ContextException(StrUtil.format(Constant.ContextExceptionMessage.CONTEXT_ALREADY_EXISTS, context.getPath()));
        }
        contextMap.put(context.getPath(), context);
    }

    public List<Context> getAllContexts(){
        List<Context> list = new ArrayList<>();
        for(Context context : contextMap.values()){
            list.add(context);
        }
        return list;
    }

    @Override
    public String toString() {
        return "Host{" +
                "contextMap=" + contextMap +
                ", hostName='" + hostName + '\'' +
                '}';
    }
}
