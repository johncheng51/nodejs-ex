package com.jm.mgr;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import com.jm.util.*;
import com.jm.xml.XmlUtil;

public class FileMgr implements OnTime {
    public  static final String   CONFIG="config";
    private Map<File, String> mapFiles  = new Hashtable();
    private Map<File, Long> mapTime     = new Hashtable();
    private File[] files;
    private File mainFolder;
    private static final String APP = "app";
    public static final File appFolder = new File(XmlUtil.getWorkDir(), APP);
    private Map<File, DoUpdate> mapUpdate = new Hashtable();

    private void init() {
        mainFolder = new File(appFolder,"");
        getMap(mainFolder);
        files = new File[mapFiles.size()];
        mapFiles.keySet().toArray(files);
        getTimeStamp();
        AdminMgr.instance().add(this, 5);
    }

    private void getTimeStamp() {
        for (File file : files) {
            Long lo = mapTime.get(file);
            Long modi = file.lastModified();
            if (lo == null)
                mapTime.put(file, modi);
            else if (lo != modi)
                mapTime.remove(file);
        }
    }

    private void getMap(File folder) {
        File[] files = folder.listFiles();
        for (File file : files) {

            if (!file.isDirectory()) {
                String text = Util.readFile(file,true);
                mapFiles.put(file, text);
            } else
                getMap(file);
        }
    }
    public String printFolder(){
        StringBuilder sb=new StringBuilder();
        sb.append("Available folder is\n");
        File[] ff=appFolder.listFiles();
        Arrays.asList(ff).stream().forEach(file->
        {sb.append(file.getName()+"\n");
                                           });
        sb.append("============================");
        return sb+"";
    }
    
    public String printFolder(String folderName) {
        File folder=new File(appFolder,folderName);
        if (!folder.exists() || folder.isFile())
        log(folder+" do not exist");
        StringBuilder sb=new StringBuilder();
        sb.append("Available File is\n");
        sb.append("============================\n");
        File[] ff=folder.listFiles();
        Arrays.asList(ff).stream().forEach(file->
        {sb.append(file.getName()+"\n");
                                           });
        sb.append("============================");
        return sb+"";
    }


    public List<Path> listFile(String name) {
        try {
            File folder = new File(appFolder, name);
            return Files.list(folder.toPath()).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public File getFolder(String name) {
        return new File(appFolder, name);
    }

    public String readFile(String folderName, String name) {
        File folder = new File(appFolder, folderName);
        File file = new File(folder, name);
        return Util.readFile(file,true);
    }

    public String readFile(DoUpdate update, String folder, String file) {
        File curFolder = new File(mainFolder, folder);
        File curFile = new File(curFolder, file);
        mapUpdate.put(curFile, update);
        String text = mapFiles.get(curFile);
        if (text == null) {
            for (File f : files) log(f + "");
            throw new Error("File not found on " + curFile);
        }
        return text;
    }

    public void writeFile(String file, String text) {
        File curFolder = new File(mainFolder, "/out/" + file);
        Util.writeFile(curFolder, text.getBytes());
    }

    public String readFile(DoUpdate update, String file) {
        return readFile(update, "", file);
    }

    private static FileMgr mgr;
    private static Object lock = new Object();

   

    public synchronized static FileMgr instance() {
        if (mgr != null)   return mgr;
        synchronized (lock) {
            mgr = new FileMgr();
            mgr.init();
            return mgr;
        }
    }

    private void log(String message) {
        System.out.println(message);
    }

    //OnTime
    public void run() {
        getTimeStamp();
        for (File file : files) {
            Long lo = mapTime.get(file);
            Long modi = file.lastModified();
            if (lo != null)
                continue;
            mapTime.put(file, modi);
            mapFiles.put(file, Util.readFile(file,true));
            DoUpdate doUpdate = mapUpdate.get(file);
            if (doUpdate != null)
                doUpdate.update();
        }
    }
}
