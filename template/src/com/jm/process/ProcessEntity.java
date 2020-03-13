package com.jm.process;
import com.jm.gen.ModelGen;
import com.jm.json.JasonParse;
import com.jm.model.*;
import com.jm.util.Util;
import java.lang.reflect.Method;


public class ProcessEntity extends ProcessWeb {
    private ClassModel curModel = null;
    private String curPack="";
    private String methodName = null;
   public ProcessEntity(String text) {super(text);}

    public void modelproc(XmlResult result) {
        JasonData jd = JasonParse.jasonData(result.value());
        String pack = result.get(PACKAGE);
        for (String modelName : jd.getList()) {
            String workListText = jd.get(modelName);
            String[] workList = Util.split(workListText, ",");
            for (String work : workList) {
                curModel = modelMgr.getClassModel(modelName);
                if (curModel==null) continue;
                String[] work_Pack = Util.split(work, "^");
                curPack = (work_Pack.length >= 2) ? work_Pack[1] : pack;
                work = work_Pack[0];
                try {
                    Method method = this.getClass().getMethod(work);
                    methodName = method.getName();
                    method.invoke(this);
                } catch (Exception e) {
                    String message=Util.getException(e);
                    if (message.indexOf("ERROR::")<0) e.printStackTrace();
                    
                }
            }
        }

    }

    public void hibernatehml() {

        FreeMarkModel fm = FreeMarkModel.ofHibernate(curModel.table(),
                           pack,curModel.cname());
        append(fm.getResult(curModel.list(), methodName));
        writeSource(pack, curModel.cname() + ".hbm.xml");
    }

    public void dbscript() {

        String primary = "";
        for (ClassField f : curModel.list())
            if (f.isPrimary()) primary = f.getName().toUpperCase();
        FreeMarkModel fm = FreeMarkModel.ofDB(curModel.table(), primary);
        append(fm.getResult(curModel.list(), methodName));
        writeFile(SCRIPT, curModel.name() + "." + SCRIPT);
    }

    public void hibernateanno()
    {
        ModelGen gen=new ModelGen(currentFolder+"",curPack);
        gen.writeAnoClass(curModel,methodName);
    }

    public void javabean() {

        ModelGen gen=new ModelGen(currentFolder+"",curPack);
        gen.writeClass(curModel,methodName);
    }
    
    public void hembed() {
        ModelGen gen=new ModelGen(currentFolder+"",curPack);
        gen.swapPackage("model","abs");
        gen.writeClass(curModel,methodName);
    }
   
    public void hid() {
        ModelGen gen=new ModelGen(currentFolder+"",curPack);
        gen.swapPackage("model","abs");
        gen.writeClass(curModel,methodName);
    }
    
    public void hinhsingle() {
        ModelGen gen=new ModelGen(currentFolder+"",curPack);
        gen.writeClass(curModel,methodName);
    }


}
