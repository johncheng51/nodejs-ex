package com.jm.xml;


public class XmlParser extends AbstractParse
{
 private boolean inBraket;
 private XmlHandler  xmlr;
 public  XmlParser(String longStr,XmlHandler xmlr)
    {  	
        super(longStr);
        this.xmlr = xmlr; 
    }

 @Override 
 public  void parse() 
    {
        char c;
        String cmd   = null; 
        String ecmd  = null; 
        String att   = null; 
        String value = null;
        String sb = "";
        boolean over = false;
        while (!over) 
        {
            c = getChar();
            switch (c) 
            {
                    default: sb += c;
                    break;
            case '<':inBraket = true;
                     value = XmlUtil.trim(sb);
                     sb = "";
                     cmd = getWord();
                     att = getAtts();
                     boolean isEndTag = false;
                    if (cmd != null) isEndTag = !xmlr.startTag(cmd, att);
                if (isEndTag) inBraket = false;
                break;
            case '/':
                if (!inBraket) 
                {
                    sb+=c;
                    break;
                }
                ecmd = getWord();
                if (ecmd != null) cmd = ecmd;
                if (!xmlr.endTag(cmd, value))  return;
                break;
            case '>':inBraket = false;
                     break;
           
            case '\n':sb+=c;
                      break;
                  case '\0': over = true;
                             break;
            
            }
        }
    }
}

