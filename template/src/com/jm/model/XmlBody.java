package com.jm.model;

import com.jm.xml.ReadHash;
import java.util.Map;

public class XmlBody
     {
        private String name;
        private Map<String,String> map;
        private String body;
        public XmlBody(String key,String atts,String body) 
        {
            this.name=key;
            map=ReadHash.get(atts);
            this.body=body;
        } 
        public Map<String,String> getMap() {return map;}

        public String get(String key) 
         {
           String text=map.get(key);
           return text==null? "":text;
         }
        public String body() {return body;}
        public String name() {return name;}
     }

