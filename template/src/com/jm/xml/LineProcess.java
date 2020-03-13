package com.jm.xml;


import com.jm.util.Util;

public class LineProcess {
    protected String[] lines;
    protected int currentPoint = 0;
    protected int size;

    public LineProcess(String text) {
        lines = XmlUtil.getLines(text);
        size = lines.length;
    }

    protected String getLine() {
        if (currentPoint >= size) return null;
        return lines[currentPoint++];
    }

    public String getStart(String pattern) {
        String result = "";
        String line = null;
        while ((line = getLine()) != null) {
            line=line.trim();
            if (!XmlUtil.startWith(line, pattern)) continue;
            result = line.substring(pattern.length());
            break;
        }
        return result;
    }

    public String getBlank() {
        String result = "";
        String line = null;
        while ((line = getLine()) != null) {
            if (Util.isBlank(line)) break;
            result += line.trim() + "\r\n";
        }
        return result;
    }

    public String getEnd(String pattern) {
        String result = "";
        String line = null;
        while ((line = getLine()) != null) {
            if (XmlUtil.startWith(line, pattern)) break;
            result += line + "\n";
        }
        return result;
    }


}


