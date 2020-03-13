package com.jm.model;

import com.jm.xml.ReadHash;

import java.util.*;

public class XmlResult {
        private String cmd;
        private Map<String,String> attData;
        private String restData; 
        public XmlResult(String cmd,String attData,String restData) 
        {
            this.cmd=cmd;
            this.attData=(new ReadHash(attData)).getTable();
            this.restData=restData;
        } 
        public String getCmd() {return cmd;}
        public Map getMap() {return attData;}
        public String value() {return restData;}
        public String get(String key) {return attData.get(key);}
    }
