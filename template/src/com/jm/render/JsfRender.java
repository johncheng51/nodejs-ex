package com.jm.render;
import com.jm.ui.*;
import com.jm.util.Util;
import java.util.*;


public class JsfRender extends AbsRender {
    public final static String AJAX     = "ajax";
    public final static String CALL     = "call";
    public final static String FORMAT   = "format";
    public final static String IMAGE    = "image";
    public final static String ARGS     = "args";
    public final static String LIST     = "list";
    public final static String METHOD   = "method";
    public final static String MAKELIST = "makelist";
    public final static String CONST    = "#";
    public final static String DOT      = ".";
    

    
    @Override
    public String printItem() {return printItem(null);}

    public String printItem(String alt) {
        alt = alt == null ? getName() : alt;
        return freeTranALT(PRINTITEM, alt);
    }

    @Override
    public String getArgs(String text){
           boolean isConst = text.startsWith(CONST);
           text=isConst? text.substring(1):text;
           boolean havedot=text.indexOf(DOT)>=0;
           if (isConst) return text;
           String result=havedot? text:beanName+"."+text;
           return "#{"+result+"}";
        }

    @Override
    public String printAction() {
        return freeTranValue(PRINTACTION, label0());
    }

    public String printAjax(String[] sa) {
        return freeTranAjax(PRINTAJAX, label0(), sa);
    }

    @Override
    public String submit() {
        String value = att(AJAX);
        if (value != null) return printAjax(split(value));
        else return printAction();
    }

    @Override
    public String msg() {
        String value = att(CALL);
        String[] words = split(value);
        String[] pair = splitsp(words[0]);
        writeFunc(pair, words[1]);
        return printItem(pair[1]);
    }

    @Override
    public String msgfmt() {

        String value = att(FORMAT);
        String args = att(ARGS);
        String[] sa = splitsp(args);
        List words = new ArrayList();
        for (String word : sa) {
            word =  getArgs(word);
            words.add(word);
        }
        return freeTranList(PRINTFMT, value, words);
    }
    
    @Override
    public String link(){
        String value = att(CALL);
        String image = att(IMAGE);
        return freeTranLink(PRINTLINK, value,image);
    }
    
    
    private String printSelect() {
        String data=att(DATA);
        String listName=getName()+LIST;
        Map map=new Hashtable();
        map.put(DATA,data);
        map.put(METHOD,cap(listName));
        writeMethod(MAKELIST, map);
        return freeTranSelect(PRINTSELECT,getName(),listName); 
    }
    
    @Override
    public String select(){return printSelect(); }
    @Override
    public String selects(){return printSelect();}
    @Override
    public String checkboxs(){return printSelect();}
    @Override
    public String checkbox(){return printItem();}
    @Override
    public String radiobutton(){return printSelect();}
    
}
