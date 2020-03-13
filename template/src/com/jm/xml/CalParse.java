package com.jm.xml;

import java.util.*;
public class CalParse extends AbstractParse{

    private static final int VALUETYPE=0;
    private static final int SPACE    =1;
    private static final int    OP    =2;
    private static final int JUNK     =3;
    private Deque valuesStack=new ArrayDeque();
    private Deque opStack=new ArrayDeque();
    public CalParse(String text)
    {
        super(text);
        parse();
    }
    @Override
    public void parse() {
        while(true) {
            char c=getChar0();
            if (c==0) 
            { 
               String cv=getValue();
               if (!isBlank(cv)) valuesStack.push(cv);
               return;
            }
            int type=getType(c);
            switch(type){
                
            case VALUETYPE: addValue(c);break;
            case SPACE:break;
            case OP:valuesStack.push(getValue());
                    pushOP(c);
                    break;
            default:  
            }
            
        }
    }
    
    private int getType(char c){
        if (Character.isAlphabetic(c) || 
            Character.isDigit(c)) return VALUETYPE;
        if (Character.isWhitespace(c)) return SPACE;
        if (isOP(c)) return OP;
        else return JUNK;
    }
    
    private void pushOP(char c){
        char cc=next();
        if (cc==0 || c=='(' || c==')') { opStack.push(c+"");return;}
        if (isOP(cc)) opStack.push(c+getChar());
        else opStack.push(c+"");
    }
    
    private boolean isOP(char c){
        String table="%*/+-=()";
        return table.contains(c+"");
    }
    
    public static void main(String[] args){
        CalParse cp=new CalParse("12+23*15");
        System.out.println(cp);
    }
   
}
