package com.jm.render;

import com.jm.gen.AbstractGen;
import com.jm.gen.ModelGen;
import java.io.File;

public class ReactRender extends HtmlRender{
 
    
    @Override
    public String react() {
       String className=cap(type,0,5);
       ModelGen mg=new ModelGen();
       String folder=threadMgr.getRenderModel().getMainFolder()+"/"+
                     threadMgr.getRenderModel().getFolder();
       mg.setWorkFolder(new File(folder));
       String text=printModel(className,new String[]{});
       mg.writeReact(text);
       writeMethod(AbstractGen.IMPORT,createValueMap(className));
       return nofunc();
    }
}
