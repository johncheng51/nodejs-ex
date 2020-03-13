package com.jm.model;


import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class ClassModel extends AbstractModel 
{
   private String discol="NONE";
    public ClassModel(XmlBody xmlBody) 
    {   super(xmlBody);   }

    
    public List<ClassField> list() {return list;}
    public String table() {return table; }
    public String params() {
        StringJoiner sj = new StringJoiner(",");
        list().forEach(f->sj.add(f.getLname()));
        return sj.toString();
    }
    public String newargs() {
        StringJoiner sj = new StringJoiner(",\n");
        list().forEach(f->sj.add(f.getType()+ " "+f.getLname()));
        return sj.toString();
    }
    
    public String newargslist() {
        StringJoiner sj = new StringJoiner("\n");
        list().forEach(f->sj.add("this."+f.getLname()+"="+f.getLname()+";"));
        return sj.toString();
    }
    
    public String props() {
        StringJoiner sj = new StringJoiner("\n");
        list().forEach(f->sj.add("protected "+f.getType()+" "+f.getLname()+";"));
        return sj.toString();
    }
    
    public Map<String,String> map(){
        Map<String,String> map=new Hashtable();
        map.put("params",params());
        map.put("props",props());
        map.put("newargs",newargs());
        map.put("newargslist",newargslist());
        map.put("discol",discol);
        map.put("uclass",this.uname());
        map.put("lclass",uname().toLowerCase());
        return map;
    }


    public void setDiscol(String discol) {
        this.discol = discol;
    }

    public String getDiscol() {
        return discol;
    }
    
    public String getId() {
       ClassField field= list().stream().filter(f->f.isId()).findFirst().get();
       return field.getName();
    }

    
}
