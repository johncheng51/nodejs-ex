package com.jm.test;


import com.jm.gen.UIGen;
import com.jm.model.MethodGen;
import com.jm.model.RenderModel;
import com.jm.process.ProcessEntity;
import com.jm.process.ProcessPath;
import com.jm.process.ProcessWeb;

public class Main extends AbstractMain{
    public MethodGen methodGen;
    public void folder() {log(fileMgr.printFolder());}
    public void folder(String name){log(fileMgr.printFolder(name));}
    
    public void file(String folder,String name){
        log(fileMgr.readFile(folder,name));
    }
    public void help() {log(readFile(HELP));}

    //entity generation
    public void entity(String db) {new ProcessEntity(db);}
    
    
    public void genModel(String folder,String file,String templateType) 
     {      methodGen=new MethodGen(folder,file,templateType);  }

    public void web(String file) {
        new ProcessWeb(file);
    }
    
    
    public void code(String command, String args){
        methodGen.write(command,args);
    }
    public void coder(String command, String args){
        methodGen.replace(command,args);
    }
    public void newmain(String command){
        methodGen.whole(command);
    }
    
    public void copy(String classname){
        methodGen.copy(classname);
    }
    //
    public void newclass(String key, String model,String type){
        methodGen.newClass(key,model,type);
    }
    public void path(String config) {
        new ProcessPath(config+".xml");
    }
    
    public void uihtml(String beanName,String templName){
        UIGen ui=new UIGen(beanName,templName);
        ui.process(RenderModel.HTML);
    }
    
    public void uijsf(String beanName,String templName){
        UIGen ui=new UIGen(beanName,templName);
        ui.process(RenderModel.JSF);
    }
    
    public void uiang(String templName){
        UIGen ui=new UIGen(templName);
        ui.processAngHtml(RenderModel.ANG);
    }
    
    public void oldang(String file){
        UIGen ui=new UIGen();
        ui.processAng1X(file);
    }
    
    public void extjs(String beanName,String templName){
        UIGen ui=new UIGen(beanName,templName);
        ui.process(RenderModel.EXTJS);
    }
    
    public void uistruts(String beanName,String templName,String altBeanName){
        UIGen ui=new UIGen(beanName,templName);
        ui.process(RenderModel.STRUTS,altBeanName);
    }
    
    public void ang(){
        UIGen ui=new UIGen();
        ui.processANG("src\\app","app");
    }
    
    public void angroute(){
        UIGen ui=new UIGen();
        ui.processRoute("src\\app","app");
    }
    
    public void react(){
        UIGen ui=new UIGen();
        ui.processReact("src","App");
    }
    
    public void moduleUpdate(){
        UIGen ui=new UIGen();
        ui.updateModule("src\\app","app");
    }
    
    public static void main(String[] args) {
        while (true) {
            String line = readLine(UIGEN);
            if (line.startsWith(QUIT)) break;
            execute(line);
        }
    }
    
    public static void mainx(String[] args) {
          //execute("moduleUpdate");
          //execute("uijsf user index");
          //execute("uistruts slogin User.pages.login loginAction");
          //execute("extjs extjs hello_world");
          //execute("uijsf error1 index");
            execute(" ");
    }


}
