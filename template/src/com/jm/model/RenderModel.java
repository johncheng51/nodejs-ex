package com.jm.model;


import com.jm.ui.AppMgr;
import com.jm.util.Util;

import java.io.File;

public class RenderModel {
    private String classExt;
    private String folder;  //working folder
    private String templateExt;
    private String beanTemplate;
    public static final String HTML   = "html";
    public static final String JSF    = "jsf";
    public static final String ANG    = "ang";
    public static final String STRUTS = "struts";
    public static final String EXTJS  = "extjs";
    public static final String REACT  = "react";
    public static final String ANG1X  = "ang1x";
    public static final String JAVA   = "java";
    public static final String PACK   = "package";
    public static final String RENDERTYPE = "rendertype";
    /*
     * The following three static variable
     * can be setting from user input
     */
    private String pack;
    private String type;
    private String mainFolder;
    AppMgr appMgr = AppMgr.service();

    public RenderModel(String type) {
        
        this.type = type;
        String text = appMgr.getP(type);
        String[] words = Util.split(text, ",");
        folder = words[0];
        templateExt = words[1].toLowerCase();
        classExt = words[2];
        beanTemplate = words[3];
        pack = words[4];
        mainFolder = appMgr.getFolder(type);
    }

    

    public String getClassExt() {return classExt;}
    public String getFolder() {return folder;}
    public String getTemplateExt() {return templateExt;}
    public void setPack(String pack) {this.pack = pack;}
    public String getPack() {return pack;}
    public String getMainFolder() {return mainFolder;}
    public String  read(String name){
        File myFile  =new File(mainFolder,folder);
        myFile     =new File(myFile,name);
        return Util.readFile(myFile,true);
    }
    
    public void  write(String name,String text){
        File myFile  =new File(mainFolder,folder);
        myFile     =new File(myFile,name);
        log("Write File:"+myFile);
        Util.writeFile(myFile, text);
    }
    
    private void log(String message){
        System.out.println(message);
    }

    @Override
    public String toString() {
        return "classExt:" + classExt + " folder:" + folder + " ext:" + templateExt + " renderType:" + type +
               " classExt:" + classExt;
    }
    public String getBeanTemplate() {return beanTemplate;}

    public String getType() {return type;}
}
