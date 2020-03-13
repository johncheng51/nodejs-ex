package com.jm.gen;

import com.jm.mgr.*;
import com.jm.model.*;
import com.jm.ui.*;
import java.io.File;
import java.util.Hashtable;
import java.util.Map;

public class UIGen extends AbstractGen {

    public UIGen() {}
    public UIGen(String templName) {this(NONE,templName);}

    public UIGen(String beanName, String templName) {
        this.beanName = beanName;
        this.templName = strToFolder(templName);

    }
    public void process(String renderType){
        process(renderType,NONE);
    }
    
    public void process(String renderType,String altBeanName) {
        threadMgr.createRenderModel(renderType);
        setPackage(beanName,altBeanName);
        String text = readUIFile(beanName);
        UIParser parse = UIParser.instance();
        UILayout layout = parse.startCompile(text);
        if (!skipModel) {
            String model = layout.printModel(altBeanName==null? beanName:altBeanName);
            ClassModel classModel = ModelMgr.instance().create(model);
            text = getBeanClass(classModel, renderType+BEANTXT);
            text += ThreadMgr.instance().readText(false);
            writeJava(text, classModel.cname());
        }
        String result = layout + "";
        log("UI:",result);
        writeAndReplace(result);
      }
    
    public void processAngHtml(String renderType) {
        threadMgr.createRenderModel(renderType);
        RenderModel renderModel=threadMgr.getRenderModel();
        String text = renderModel.read(templName + ANGHTMLTEMP);
        UIParser parse = UIParser.instance();
        UILayout layout = parse.startCompile(text);
        text=layout+"";
        String prog=layout.printJS("myTable");
        threadMgr.add("prop",prog);
        renderModel.write(templName + ANGHTML, text);
        text = renderModel.read(templName + ANGCLASSTEMP);
        log("CLASS",text);
        Map map=threadMgr.getMap();
        String result=this.freeMarker.tranWithText(text, map);
        log("HTML",result);
        renderModel.write(templName + ANGCLASS, result);
    }
   /**
     * There is Main Folder for application
     * @param sourceFolder: Where is template load from this folder
     * example: "src/app"
     * @param template: the file will be used
     * example: app
     */
    public void processANG(String sourceFolder, String template) {

        threadMgr.createRenderModel(RenderModel.ANG);
        setWorkFolder(sourceFolder);
        String text = readFile(template + ANGHTML);
        log("processANG:",text);
        UIParser parse = UIParser.instance();
        UILayout layout = parse.startCompile(text);
        log("WebLayout", layout + "");
        text=readFile(template + ANGCLASS+TEMPLATE);
        text=text.replace(METHODMARK,threadMgr.readText(false)+RTN+METHODMARK);
        File file=new File(workFolder,template + ANGCLASS);
        writeFile(file, text);
     }
    /**
      * There is Main Folder for application
      * @param sourceFolder: Where is template load from this folder
      * example: "src/app"
      * @param template: the file will be used
      * example: app
      */
    public void processRoute(String sourceFolder, String template) {
        threadMgr.createRenderModel(RenderModel.ANG);
        setWorkFolder(sourceFolder);
        String text = readFile(template + ANGHTMLTEMP);
        log("processANG:",text);
        UIParser parse = UIParser.instance();
        UILayout layout = parse.startCompile(text);
        text=layout + "";
        log("WebLayout", text);
        File file=new File(workFolder,template + ANGHTML);
        writeFile(file, text);
        String myTemplate=freeMarker.readTemplate(RenderModel.ANG, MODULE);
        Map map=threadMgr.getMap();
        text=freeMarker.tranWithText(myTemplate, map);
        file=new File(workFolder,template + ANGMODULE);
        writeFile(file, text);
    }
   
   /*
    * under src folder src 
    * get the file App.js.templ data and convert to App.js
    * create <ReactNote/><ReactNote1/> multiple class
    */
    public void processReact(String sourceFolder, String template) {

        threadMgr.createRenderModel(RenderModel.REACT);
        setWorkFolder(sourceFolder);
        String text = readFile(template + REACT);
        log("processANG:",text);
        UIParser parse = UIParser.instance();
        UILayout layout = parse.startCompile(text);
        log("WebLayout", layout + "");
        Map map=new Hashtable();
        map.put(IMPORT,threadMgr.readText(false));
        String result=freeMarker.tranWithText(text, map);
        writeFile(getFile(template+JS),result);
    }
    
    public void processAng1X(String template){
        threadMgr.createRenderModel(RenderModel.ANG1X);
        setWorkFolder("");
        String text = readFile(template + HTMLTEMPLATE);
        log("Data:",text);
        UIModel uiModel=new UIModel(text);
        UIParser parse = UIParser.instance();
        UILayout layout = parse.startCompile(uiModel.getTemplate());
        String layoutData=layout+"";
        log("Template:",layoutData);
        String result=freeMarker.tranWithText(uiModel.getMain(),"body",layoutData);
        File file=new File(workFolder,template + HTML);
        writeFile(file, result);
}
   
    public void updateModule(String sourceFolder, String template) {
        threadMgr.createRenderModel(RenderModel.ANG);
        File folder=setWorkFolder(sourceFolder);
        FolderModel fm=new FolderModel(folder,ANGCLASS);
        Map map=fm.getMap();
        String data=freeMarker.transByFileName("angmodule",map);
        writeModule(template,data);
    }
}
