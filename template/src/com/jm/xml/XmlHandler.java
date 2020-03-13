package com.jm.xml;


public interface XmlHandler 
{
     boolean startTag(String cmd, String att);
     boolean endTag(String cmd, String value);
}
