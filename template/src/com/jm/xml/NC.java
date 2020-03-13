package com.jm.xml;

public class NC
{
    public static final    char CEND       = '\0';
    public static final char CSP = ' ';
    public static final char CQQ = '\'';
    public static final char CRTN = '\n';
    public static final String CHILD = "child";
    public static final String NOUSE = "nouse";
    public static final String UTIL_TD = "td";
    public static final String GWTFXID = "gwtfxid";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String NAME = "name";
    public static final String STYLE = "style";
    public static final String TEXT = "text";
    public static final String MAIN = "main";
    public static final String SCRIPT = "script";
    public static final String SCRIPT1 = "script1";
    public static final String MAINPRG = "../Main.xml";
    public static final String CSFX = "\\javafx\\";
    public static final String CONFIG = "Config.properties";
    public static final int THREESECOND = 60;
    //for MacroMgr
    public static final String SALL = "all";
    public static final String GET = "get";
    public static final String SET = "set";
    public static final String EXE = "exe";
    public static final String SVALUE = "value";
    public static final String PARAMFILE = "Param";

    public static final String MMAP = "map";
    public static final String MMACRO = "macro";
    public static final String MCOLOR = "color";
    //for ParseMap
    public static final String FLEXMAP = "FlexMap";
    public static final String SMAPSTART = "<map";
    public static final String SMAPEND = "</map>";
    public static final String MARK = "###";
    public static final String SCLASS = "class";
    public static final String SPARAM = "param";
    //CSAction
    public static final String CS = "cs.";
    public static final String SME = "me";
    //For Token
    public static final String TokenFUNC = "tfunc";
    public static final String TokenDOT = "tdot";
    public static final String TokenNONE = "NONE";
    //ScriptMgr 
    public static final String SM_FLEXGET = "flexGet ";
    public static final String SM_EVAL = "eval";
    public static final String SM_MYFLEXDATA = "myFlexData";
    public static final String SM_FLEXSET = "flexSet ";
    public static final String SM_FLEXRETURN = "flexReturn";
    public static final String SM_FLEXTEST = "flexTest";
    //LabelFunction
    public static final String LABELFUNCTION = "labelFunction";
    public static final String ITEMRENDERER = "itemRenderer";
    //ActionForm
    public static final String FORM     = "form";
    public static final String FORMUI   = "ui";
    public static final String FORMTOOL = "tool";
    //ToType
    
    public static boolean isBlank(String s) {
      return s==null || s.trim().length()==0 || s.trim().toLowerCase().equals("null");
  }
    
     

}
