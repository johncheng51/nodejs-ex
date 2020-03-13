package com.jm.ui;

import com.jm.mgr.FileMgr;
import com.jm.util.Util;
import java.io.File;
import java.util.Hashtable;
import java.util.Map;

public class UIFileMgr {

    FileMgr fileMgr = FileMgr.instance();

    public String readFile(String folderName, String file) {
        return fileMgr.readFile(folderName, file);
    }

    public File getFolder(String file) {
        return fileMgr.getFolder(file);
    }

    public Map<String, String> allFiles(String file) {
        Map<String, String> map = new Hashtable();
        File folder = getFolder(file);
        File[] ff = folder.listFiles();
        for (File f : ff) {
            String data = Util.readFile(f,true);
            String[] pair = Util.split(f.getName(), ".");
            map.put(pair[0], data);
        }
        return map;
    }


    private static UIFileMgr mgr;
    private static Object lock = new Object();

    public synchronized static UIFileMgr instance() {
        if (mgr != null)
            return mgr;
        {
            synchronized (lock) {
                if (mgr != null)
                    return mgr;
                mgr = new UIFileMgr();
                return mgr;
            }
        }
    }
}
