package com.jm.model;

import com.jm.gen.ModelGen;
import com.jm.mgr.ModelMgr;

public class MethodGen
{
    private String folder;
    private String name;
    private String pack;
    private String fileName;
    private String templateType="";
    private String   sourceType;
    private static final String SRC="src";
    private static final String DOT=".";
    private ModelMgr modelMgr=ModelMgr.instance();
    private ModelGen modelGen;
    public MethodGen(String folderloc,String fileName,String templateType) {
        int n=folderloc.indexOf(SRC);
        folder="C:\\"+folderloc.substring(0,n-1);
        folder=folder.replaceAll("\\.", "\\\\");
        pack=folderloc.substring(n+4);
        this.name    =fileName;
        this.fileName=fileName; 
        if (this.fileName.indexOf(".")<=0) this.fileName+="."+templateType;
        this.templateType=templateType+".txt";
        this.sourceType  =templateType;
        modelGen=new ModelGen(folder,pack);
    }
    
    public void write(String command,String args){
        modelGen.writeCode(command, args, fileName,templateType);
    }
    
    public void replace(String command,String args){
        modelGen.replace(command, args, fileName,templateType);
    }
    
    public void whole(String command){
        modelGen.writeWhole(command, fileName,templateType);
    }
    
    public void copy(String className){
        modelGen.copy(className, fileName,name,sourceType);
    }
    
    public void newClass(String key,String modelName,String type){
        ClassModel model = modelMgr.getClassModel(modelName);
        String result    = modelGen.getNewClass(model, key,templateType);
        String myName  = model.cname()+"."+sourceType;
        modelGen.newClass(result, myName,type);
    }
    
    public void setFile(String file){
        
    }
    
    
}
