package com.jm.model;


import com.jm.util.Util;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FolderModel {
    public static final String COMPONENT="Component";
    public static final String COMPONENTS="components";
    public static final String DOT=".";
    public static final String LIST="list";
    private List<File> files = new ArrayList();
    public File curFolder;

    /*
     * Only get child folder recursive.
     */
    public FolderModel(File folder, String match) {
        curFolder = folder;
        File[] childs=folder.listFiles(f->f.isDirectory());
        Arrays.asList(childs).stream().forEach(f->{
            collect(f,match);                                    
        });
    }
   
    private void collect(File folder, String match) {

        File[] dirs = folder.listFiles((f) -> {
                if (f.getName().lastIndexOf(match) > 0)
                    files.add(f);
                return f.isDirectory();
            });
        for (File dir : dirs)
            collect(dir, match);
    }

    private List<Folder> getFolder() {
        List<Folder> result=  files.stream().map(
         f -> new Folder(f)).collect(
           Collectors.<Folder> toList());
        return result;
    }
    
    public Map getMap(){
        Map map=new Hashtable();
        List<Folder> list=getFolder();
        map.put("list",list);
        String result="";
        int count=0;
        for (Folder f:list) 
        {
            result+=f.component;
            result+= list.size()==(count+1)? " ":",";
            result+="\n";
            count++;
        }        
        map.put(LIST,list);
        map.put(COMPONENTS,result);
        return map;
    }

    public class Folder {
        private String classname;
        private String lclassname;
        private String folder;
        private String component;

        public Folder(File file) {
            String text = file + "";
            String fileName = file.getName();
            String[] sa = Util.split(fileName, DOT);
            classname  = Util.cap(sa[0]);
            lclassname = sa[0].toLowerCase();
            component  = classname+COMPONENT;
            int n=text.indexOf(fileName);
            folder=text.substring((curFolder+"").length()+1,n-1);
        }

        public String getClassname() {return classname;}
        public String getLclassname() {return lclassname;}
        public String getFolder() {return folder;}
        public String getComponent() {return component;}
    }
}
