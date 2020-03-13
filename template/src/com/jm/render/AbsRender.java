package com.jm.render;

import com.jm.ui.Menu;
import com.jm.util.ThreadUtil;
import java.util.Map;

public abstract class AbsRender extends  BasicRender{
   public static final String   SINGLE="single";
   public static final String   ROW_TYPE="ROW_TYPE";
   public static final String   TABLE="table";
   public static final String   TABLE_ROW="table_row";
    public static final String  DIV_ROW="div_row";
   public static final String   CONTAINER_ROW="container_row";
   public static final String   ATTR="attr";
   public static final String   BODY="body";
   public static final String  CONTAIER    ="container";
   
    public String menu() {
        String str = value;
        String attr=this.getAttStr();
        boolean isSingle=attr.indexOf(SINGLE)>=0;
        Menu menu = new Menu(str,this,isSingle);
        return menu + "";
    }
    
    public String table(){
        String attr = getAttStr();
        ThreadUtil.putValue(ROW_TYPE, TABLE);
        String body =appendChild(false);
        Map map=this.createMap(ATTR,BODY,attr,body);
        ThreadUtil.remove(ROW_TYPE);
        String text=freeTran(TABLE, map);
        return text;
    }
    
    public String container(){
        String attr = getAttStr();
        ThreadUtil.putValue(ROW_TYPE, CONTAIER);
        String body =appendChild(false);
        Map map=this.createMap(ATTR,BODY,attr,body);
        ThreadUtil.remove(ROW_TYPE);
        return this.freeTran(CONTAIER, map);
    }
            
    public String row() {
        boolean isTable=ThreadUtil.isTable();
        String attr = getAttStr();
        String body =appendChild(true);
        Map map=createMap(ATTR,BODY,attr,body);
         String text= this.freeTran(isTable? TABLE_ROW:CONTAINER_ROW,map);
         return text;
       }
    
    
}
