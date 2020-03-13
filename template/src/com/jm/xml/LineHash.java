package com.jm.xml;

import com.jm.util.Util;
import java.util.*;


/**
 * Created by John on 3/31/2017.
 */
public class LineHash extends LineProcess {
    private Hashtable<String, String> map = new Hashtable();

    public LineHash(String text) {
        super(text);
        process();
    }

    private void process() {
        while (true) {
            String key = getStart("$$_");
            if (Util.isBlank(key)) break;
            String value = getEnd("__end");
            map.put(key.trim(), value);
        }
    }

public static Map<String,String> getMap(String text){
    LineHash lineHash=new LineHash(text);
    return lineHash.map;
}
}
