package com.jm.gen;

import com.jm.mgr.*;
import com.jm.model.*;
import com.jm.ui.*;
import com.jm.util.Util;
import java.util.Hashtable;
import java.util.Map;


/*
 * This use for application on the folder and
 * src file is under the folder
 */
public class ModelGen extends AbstractGen {
    public static final String INPUTMARK="###input###";
    public static final String OUTPUTMARK="###output###";
    public static final String IMPORT="import";
    public static final String HTML="html";
    public static final String PUTHERE="//put-here";
    public ModelGen() {}
    public ModelGen(String folder, String pack) {super(folder, pack);}
    public ModelGen(String folder, String pack, String srcFolder) {
        super(folder, pack, srcFolder);

    }

    public void writeClass(ClassModel model, String type) {
        String text = getBeanClass(model, type);
        writeJava(model.cname(), text);
    }
   
    public void writeAng(String text,String input,String html,String publish) {
        ClassModel model = modelMgr.create(text);
        String result = getBeanClass(model, RenderModel.ANG+BEANTXT);
        result=result.replace(INPUTMARK,input);
        result=result.replace(OUTPUTMARK,publish);
        writeANG(threadMgr.getWorkFolder(),model.name(), result,html);
    }
    
    public void writeRoute(String text,String param) {
        ClassModel model = ModelMgr.instance().create(text);
        String result = getAngClass(model, BEANTXT,param);
        String myHTML=freeMarker.readTemplate(RenderModel.ANG, HTML);
        UIParser parse = UIParser.instance();
        UILayout layout = parse.startCompile(myHTML);
        writeANG(threadMgr.getWorkFolder(),model.name(), result,layout+"");
    }
    
    //String type:
    public void newClass(String result,String fileName,String append){
         if (append.equals("append")){
            String fileData=readSource(fileName).trim();
            fileData=Util.removeLast(fileData);
            writeSource(fileName, fileData+result+"\n}");
        }
        else writeSource(fileName, result);
    }
    
    public void writeReact(String text) {
        ClassModel model = modelMgr.create(text);
        String result = getBeanClass(model, RenderModel.REACT+BEANTXT);
        writeJS(model.name(), result);
    }

    public void writeAnoClass(ClassModel model, String type) {
        String text = getBeanClass(model, RenderModel.JAVA+BEANTXT);
        text = swapMgr.replace(text, IMPORT, type);
        text = swapMgr.replace(text, model, type);
        writeJava(model.cname(), text);
        swapPackage("model","dao");
        text = getBeanClass(model, "dao");
        writeJava(model.cname()+"Dao", text);
        
    }
    
    public void writeCode(String methodKey,
                          String args,
                          String fileName,
                          String templateType){
        String template=swapMgr.getCodeTemplate(methodKey, templateType);
        String[] words=Util.split(args," ");
        String result=freeMarker.tranWithText(template, words);
        String name=fileName;
        String fileData=readSource(name).trim();
        fileData=Util.removeLast(fileData);
        writeSource(name, fileData+result+"\n}");
    }
    
    public void writeWhole(String methodKey,
                           String fileName,
                           String templateType){
        String template=swapMgr.getCodeTemplate(methodKey, templateType);
        Map map=new Hashtable();
        map.put("package",this.pack);
        map.put("class",Util.getFirst(fileName, "."));
        String result=freeMarker.tranWithText(template, map);
        writeSource(fileName, result);
    }
    
    public void copy(String className,
                           String fileName,
                           String name,
                           String templateType){
        String fileData=readSource(fileName).trim();
        fileData =fileData.replaceAll(name, className);
        log("",fileData);
        writeSource(className+"."+templateType, fileData);
    }
    
    public void replace(String methodKey,
                          String args,
                          String fileName,
                          String templateType){
        String template=swapMgr.getCodeTemplate(methodKey, templateType);
        String[] words=Util.split(args," ");
        String result=freeMarker.tranWithText(template, words);
        String name=fileName;
        String fileData=readSource(name).trim();
        fileData=fileData.replace(PUTHERE, result+PUTHERE);
        writeSource(name, fileData);
    }
}
