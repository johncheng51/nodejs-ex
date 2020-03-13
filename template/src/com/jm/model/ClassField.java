package com.jm.model;
import com.jm.mgr.ModelMgr;
import com.jm.util.Util;
import java.text.SimpleDateFormat;
import java.util.*;

/*     p.i.id:i            p: primary Key i: ID  integer
      p.ie.id:@Address     with enbed
         name:s
         name:@ss          Collection of Element
name.@Address:@ss          Collection of Embed  
name.@Address:m2m,o2o,o2m,m2o One To many 
        
 */
public class ClassField extends AbstractField {

    private boolean _isPrimary;
    private boolean _isId;
    private boolean _isVersion;
    private boolean _isAuto;
    private boolean _isColl;
    private String require = " required ";
    private String subType;
    private String size;
    private String max;
    private String min;
    private String colName = "";
    
    /**
     * Use for Object such as Set,List,Map and Collection
     */


    private String op;
    private String assoFieldName;
    private String assoColName;
    private String assoTable;
    private String assoClass;
    private String op_arg;

    public ClassField(AbstractModel model,String name, String text) {
        super(model,name, text);
    }

    protected void processField(String main, String value) {
        checkPrimary(main);
        if (colName == null) colName = name;
        String[] sa = splitCommon(value);
        String stype = (sa.length >= 2) ? sa[1] : sa[0];
        processType(sa[0]);
        processSType(stype);

    }
    
    private String g(char c,String text){
        String myType="";
        switch (c) {
            case 'i':myType = "Integer";break;
            case 'l':myType = "Long";break;
            case 'd':myType = "Double";break;
            case '@':myType=text.substring(2);break;
               default: 
               case 's':myType = "String";break;
           }
        return myType;
    }
    

    private void processType(String text) {
        switch (text.charAt(0)) {
            case '*':type = this.getCname();break;
            case '#':type =  text.substring(1);break;
            case 'i':type = "Integer";break;
            case 'l':type = "Long";break;
            case 'd':type = "double";break;
            case 'D':type = "Date";break;
            case 'c':type = "Calendar";break;
            case '@':if (_isId) type=text.substring(1);
                     else       type=makeColl(text);break;
            default:type  = "String";break;
        }
        if (text.length()>=2 && text.charAt(1)=='a')
        type+="[]";
    }
    
    private String makeColl(String text) {
        _isColl=true;
        String myType="";
        text=text.substring(1);
        char first=text.charAt(0);
        char second=text.charAt(1);
        String subype=g(second,text);
        switch(first){
        case 's': myType="Set<"+subype+">";op="coll_set";break;
        case 'l': myType="List<"+subype+">";op="coll_list";break;
        case 'm': myType="Map<String,"+subype+">";op="coll_map";break;
         default: myType="String";
            }
        return myType;
    }

    protected void processObject(String main, String value) {
        if (main.length() == 0) objName = name;
        else objName = main;
        model.setMapby(objName);
        String[] words = splitCommon(value);
        String first = fillSpace(words[0],5);
        assoFieldName = words.length==1? first:words[1];
        if (words.length == 3) assoColName = words[2];
        else assoColName=assoFieldName.toUpperCase();
        boolean isOver=first.charAt(0)=='@';
        if (isOver) { setOverride(first);return;}
        String op1=""+first.substring(0,3);
        switch (op1) {
            case "o2m":op="onetomany";
                      classType="Set<"+cap(name)+">";
                      break;
            case "m2o":op="manytoone";
                      classType=cap(name);break;
            case "o2o":classType=cap(name);
                      boolean isSlave=first.charAt(2)=='x';
                      op="onetoone_"+(isSlave? "slave":"master");
                      break;
            case "m2m":op="manytomany";
                       classType="Set<"+cap(name)+">";break;
         }
        if (isObj()) {
            assoClass=getClassType();
            assoTable="Dept";
        }
    }
    
    private String fillSpace(String text,int n){
        if (text.length()>n) return  text;
        String result=text;
        for (int i=text.length();i<n;i++) result+="x";
        return result;
        
    }
    
    private void setOverride(String text){
        op=getOverColl(text);
        classType=cap(name);
        ClassModel model=ModelMgr.instance().make(name);
        if(model!=null) myList=model.list();
        for(ClassField f:myList)
        f.setOverhead(assoFieldName.toUpperCase());
        if (isObj()) {
            assoClass=getClassType();
            assoTable="Dept";
        }
    }
    
    //Modify for Collection with 
    private String getOverColl(String text){
        String myType="";
        String myOp="overrides"; 
        _isColl=true;
        switch(text.charAt(1)){
        case 's': myType="Set<"+cap(name)+">";myOp="coll_set";break;
        case 'l': myType="List<"+cap(name)+">";myOp="coll_list";break;
        case 'm': myType="Map<String,"+cap(name)+">";myOp="coll_map";break;
        default:  _isColl=false;myType=cap(name);myOp="";break;
        }
        if (isColl()) myOp+=".";
        myOp+="overrides";
        type=myType;
        return myOp;
    }
    
    

    private void processSType(String text) {
        switch (text.charAt(0)) {
            case 'e':subType = "email";break;
            case 'i':subType = "number";break;
            case 'r':subType = "range";break;
            case 't':subType = "time";break;
            case 'u':subType = "url";break;
            case 'f':subType = "decimal";break;
            default:subType = "text";break;
        }
    }

    public String getDbtype() {
        switch (type.toLowerCase()) {
            case "string":return "varchar(255)";
            case "int": return "int";
            default:return "NULL";
        }

    }

    public String getColname() {
        if (isObj()) return this.assoColName;
        colName = Util.isBlank(colName) ? this.name : colName;
        return colName.toUpperCase();
    }


    private void checkPrimary(String name) {
        String[] words = splitd(name);
        for (String word : words) {
            char c = word.charAt(0);
            String value = getInt(word);
            switch (c) {
                case 'p': _isPrimary = true;break;
                case 'i': _isId = true;op_arg=word;break;
                case 'n':require = null;break;
                case 'c':colName = word.substring(1);break;
                case 's':if (value != null) size = " size=" + q(value);break;
                case 'm':if (value != null) min = " min=" + q(value);break;
                case 'a':_isAuto=true;op_arg=word;break;
            case 'v':_isVersion=true;op_arg=word;break;
            case 'M':
                    if (value != null) max = " max=" + q(value);
                    break;
            }
        }
    }

    public boolean isPrimary() {
        return _isPrimary;
    }

    public boolean isId() {
        return _isId;
    }
    
    public boolean isVersion() {
        return _isVersion;
    }
    
    public boolean isAuto() {
        return _isAuto;
    }
    
    public boolean isColl() {
        return _isColl;
    }

    public String getSubType() {
        return subType;
    }


    public String getClassType() {
        return classType;
    }

    public String getOp() {
        if (isObj() || isColl()) return op;
        else if (isId()) return getId();
        else if (isVersion()) return getVersion();
        else if (isAuto())  return "autodate";
        else return "prop";
    }
    
    private String getVersion() {
        return op_arg.equals("v")? "version":"version_t";
    }
    
    private String getId(){
        String key="";
        switch(op_arg){
        case "iu":key="id_uuid";break;
        case "is":key="id_seq";break;
        case "ii":key="id_inc";break; 
        case "it":key="id_table";break; 
        case "ie":key="id_emb";break;
        case "if":key="id_fore";break;
        default:key="id";break;
        }
        return key;
    }

    public String rest() {
        String result = "";
        if (size != null) result += size;
        if (min != null) result += min;
        if (max != null) result += max;
        if (require != null) result += require;
        return result;
    }

    public String getObjName() {
        return objName;
    }
    public String getAssoclass() {
        return assoClass;
    }

    public String getAssotable() {
        return assoTable;
    }
    
    public String sample(){
        SimpleDateFormat sdf=new SimpleDateFormat("DD-mm-yyyy hh:MM:SS");
        Date date=new Date();
        return sdf.format(date);
    }
    
    public String classname(){
        return model.uname();
    }
    
    public List<ClassField> list() {
        return myList;
    }
    
    private String overhead="";
    public void setOverhead(String overhead) {
        this.overhead = overhead;
    }

    public String getOverhead() {
        return overhead;
    }
    
    public Map<String,String> map(){
        ClassModel newModel=(ClassModel) model;
        Map<String,String> map=new Hashtable();
        map.put("classname",newModel.uname());
        map.put("id",newModel.getId());
        map.put("mapby",newModel.getMapby());
        map.put("sample",sample());
        return map;
    }
}
