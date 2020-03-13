package com.jm.ui;
import com.jm.util.Util;
import java.util.*;

public class UISimple {
    private UILayout layout;
    private String text;
    private Map<String, String> currentAtts;
    private UICompile compile;
    private UIParser uiParser;
    public static final String PIPE="|";
    public static final String SEP =".";
    public static final String TYPE="type";
    public static final String NOSPACE="ns";
    public UISimple(UILayout layout,
                    String text,
                    Map map,
                    UICompile compile) {
        this.layout=layout;
        this.text=text;
        currentAtts=map;
        this.compile=compile;
        uiParser=compile.getUiParse();
        process();
    }
    
    private void process(){
        String isRow=currentAtts.get("isrow");
        layout.removeAtt("isrow");
        String defaultType=currentAtts.get(TYPE);
        layout.removeAtt(TYPE);
        defaultType=defaultType==null? UILayout.UI_TEXT:defaultType;
        String myType= UIData.getType(defaultType);   
        if (isRow!=null) process_row(myType);
        else process_table(myType);
    }
    
    private void process_table(String type) {
        layout.setType(UILayout.UI_ARRAY);
        String[] words = Util.split(text, PIPE);
        Arrays.asList(words).forEach(word->{
        UILayout row=uiParser.create(UILayout.UI_ROW);
        String[] elements=Util.split(word,SEP);
        boolean haveDot=elements.length>=2; 
        String myName=haveDot?  elements[1]:word;                               
        String ctype =haveDot? UIData.getType(elements[0]):type;
        String value =elements[elements.length-1];                                
        row.add(uiParser.create(UILayout.UI_LABEL,value.toUpperCase()));
        row.add(uiParser.create(ctype, myName, value));
        layout.add(row);                                                              
                                     });
    }
    
    private void process_row(final String type) {
        boolean nospace=currentAtts.get(NOSPACE)!=null;
        
        layout.removeAtt(NOSPACE);
        layout.setType(UILayout.UI_ROW);
        UILayout container=uiParser.create(UILayout.UI_ARRAY); 
        layout.add(container);
        String[] words = Util.split(text, PIPE);
        Arrays.asList(words).forEach(word->{
            String[] elements=Util.split(word,SEP);
            boolean haveDot=elements.length>=2;
            String ctype =haveDot? UIData.getType(elements[0]):type;                        
            String myName=haveDot?  elements[1]:word; 
            String value =elements[elements.length-1];                         
            if (!nospace) container.add(uiParser.create(UILayout.UI_LABEL,"&nbsp;&nbsp;&nbsp;"," "));                        
            container.add(uiParser.create(ctype, myName, value));
             
        });       
    }
    
    public static void loadSimple(UILayout layout,
                    String text,
                    Map map,
                    UICompile compile) {
        new UISimple(layout,text,map,compile);
    }
    
    public static void main(String[] args){
        String text="<simple>"+
                    "firstName|lastName|MiddleName</simple>";
        UIParser uiParse = UIParser.instance();
        UILayout layout = uiParse.compile(text);
        System.out.println(layout.printTree(" "));
        System.out.println(layout);
    }
}
