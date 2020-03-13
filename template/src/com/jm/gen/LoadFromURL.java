package com.jm.gen;

import com.jm.model.FilesFromURL;
import java.io.File;

import java.util.List;
import java.util.Map;

public class LoadFromURL extends AbstractGen{
    private String urlRoot;
    private String folder;
    public LoadFromURL(String urlRoot,String folder ) {
        this.urlRoot=urlRoot;
        this.folder=folder;
        process(urlRoot,folder);
    }
    
    private void process(String currentURL,String currentFolder){
        List<String> urlFolder=writeJavaSource(currentURL,currentFolder);
        urlFolder.stream().forEach(url->{
            String urlText= currentURL+"/"+url;
            String folder = currentFolder+"/"+url;                      
            process(urlText,folder);                      
        });
    }
    
    private List<String> writeJavaSource(String currentURL,String currentFolder){
        FilesFromURL files=new   FilesFromURL(currentURL);
        Map<String,String> map=files.loadJava();
        map.forEach((filetext,text)->{
           File file=new File(currentFolder,filetext);         
           this.writeFile(file, text); 
        });
        return files.getFolder();
    }
    
    public static void main(String[] args){
       LoadFromURL load=new  LoadFromURL("https://www.cs.rit.edu/~ark/pj/lib/","pj");
    }
    
}
