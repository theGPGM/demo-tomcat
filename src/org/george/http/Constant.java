package org.george.http;

import cn.hutool.system.SystemUtil;

import java.io.File;

public class Constant {
    public final static File webappsFolder = new File(SystemUtil.get("user.dir"), "webapps");
    public final static File rootFolder = new File(webappsFolder, "ROOT");
}
