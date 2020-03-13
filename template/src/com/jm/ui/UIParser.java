package com.jm.ui;


import com.jm.mgr.ThreadMgr;
import com.jm.model.RenderModel;
import com.jm.render.*;
import com.jm.util.Util;
import com.jm.xml.XmlList;

import java.lang.reflect.Constructor;

import java.util.*;

public class UIParser {

    public static final String RENDERPACK="com.jm.render.";
    public static final String RENDER="Render";
    private Map<String, String> funcMap;
    private ThreadMgr threadMgr=ThreadMgr.instance();
    private boolean haveError=false;
    private UIParser() {}

    public UILayout startCompile(String text) {
        SplitProcess sp = new SplitProcess(text, this);
        funcMap = sp.funcMap;
        UILayout layout=sp.getLayout();
        if (haveError ) {
            String message=layout+"";
            throw new RuntimeException("There is Error:"+message);
             }
        else return layout;
    }

    public UILayout compile(String text) {
        UICompile compile = new UICompile(text, this);
        UILayout layout= compile.getLayout();
        if (haveError || compile.isError()) {
            String message=layout+"";
            throw new RuntimeException("There is Error:"+message);
             }
        else return layout;
    }
    
    public UILayout createClass(String name,String param) {
           UILayout layout=null;
        try {
            Class cs = Class.forName(name);
            Constructor con = cs.getConstructor(new Class[] { String.class });
            layout=(UILayout) con.newInstance(new Object[] { param });
            
        } catch (Exception e) 
        {
            layout= create(UILayout.UI_ERROR, name, "Can not find the class");
        }
        setLayout(layout);
        return layout;
    }  
    
    public UILayout create(String text) {
        UILayout layout = new UILayout(text);
        setLayout(layout);
        return layout;
    }
    

    public void setLayout(UILayout layout) {
        AbsRender render=null;
        String type=RenderModel.HTML;
        try {type=threadMgr.getRenderModel().getType();}
        catch(Exception e){}
        try{
           Class myClass=Class.forName(RENDERPACK+Util.cap(type)+RENDER);
           render=(AbsRender) myClass.newInstance(); 
        }
        catch(Exception e) {e.printStackTrace();}
        render.setLayout(layout);
        render.setFuncMap(funcMap);
        render.setRenderType(type);
        layout.setUiRender(render);
       
    }

    public UILayout create(String text, String value) {
        UILayout layout = create(text);
        layout.setValue(value);
        return layout;
    }

    public UILayout create(String tagName, String value, String name) {
        UILayout layout = create(tagName, value);
        layout.setName(name);
        return layout;
    }


    public static UIParser instance() {
        return new UIParser();
    }

    public static class SplitProcess {
        private UILayout layout;
        public Map<String, String> funcMap = new Hashtable();
        private UIParser parser;
        private String mainText;

        public SplitProcess(String text, UIParser parser) {
            this.parser = parser;
            init(text);
        }

        private void init(String text) {
            if (text==null){
                System.exit(0);
            }
            int n = text.indexOf("=============");
            if (n < 0) {
                mainText = text;
                return;
            }
            mainText = text.substring(0, n);
            String rest = text.substring(n + 1);
            XmlList xmlList = new XmlList(rest, "method");
            funcMap = xmlList.getTable();
        }

        public UILayout getLayout() {
            UICompile compile = new UICompile(mainText, parser);
            try {
                layout = compile.getLayout();
            }
            catch(Exception e) {
                
                }
            if (compile.isError()) parser.haveError=true;
            return layout;
        }


    }
}
