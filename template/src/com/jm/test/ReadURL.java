package com.jm.test;

import com.jm.util.Util;

public class ReadURL {
    public static void main(String[] args) {
      byte[] bytes= Util.readUrl("https://www.cs.rit.edu/~ark/pj/");
      String text=new String(bytes);
      System.out.println(text);
    }
        
}
