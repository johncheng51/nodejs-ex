package com.jm.json;

import com.jm.model.JasonData;
import com.jm.xml.AbstractParse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JasonParse extends AbstractParse {
    private boolean haveKey = false;
    private boolean haveValue = false;
    private String key = "";
    private String value = "";
    private HashMap map = new HashMap();
    private ArrayList<String> list = new ArrayList();

    public JasonParse(String data) {
        super(data);
        parse();
    }


    public void show() {
        for (String key : list)
            System.out.println(key + ":" + map.get(key) + "\n");
    }

    private void clear() {
        haveKey = false;
        haveValue = false;
        key = "";
        value = "";
    }

    private void add(String key, String value) {
        value=value.toLowerCase().equals("none")? "":value;
        map.put(key, value);
        list.add(key);
        clear();
    }

    private void add(String value) {
        String key = "$ins" + map.size();
        add(key, value);
    }

    @Override
    public void parse() {
        while (true) {
            char c = getChar0();
            switch (c) {

                case ':':
                    haveKey = true;
                    break;
                case 13:
                case 10:
                case '\t':
                case ' ':
                    if (haveValue) add(key, value);
                    break;
                case '`':
                case '\"':
                case '\'':
                    value = skipToChar(c);
                    haveValue = true;
                    break;
            case '@':value = '#'+skipToChar('@');
                     add(key, value);
                    break;
                case '[':
                    value = skipToChar(']');
                    value = "[" + value + "]";
                    haveValue = true;
                    break;
                case '{':
                    value = skipToChar('}');
                    value = "{" + value + "}";
                    haveValue = true;
                    break;
                case '!':
                    value = skipToChar('\n');
                    add(value);
                    break;
                case '\0':return;
                case '#':c = ':';
                default:
                    if (!haveKey) key += c;
                    else {
                        haveValue = true;
                        value += c;
                    }
                    break;
            }
        }
    }

    public Map<String, String> getMap() {
        return map;
    }

    public List<String> getList() {
        return list;
    }

    public static String removeB(String text) {
        text = text.trim();
        if (text.charAt(0) == '{') {
            text = text.substring(1, text.length() - 1);
        }
        return text;
    }

    public static Map<String, String> getMap(String text) {
        text = removeB(text);
        JasonParse jp = new JasonParse(text);
        return jp.getMap();
    }

    public static JasonData jasonData(String text) {
        text = removeB(text);
        JasonParse jp = new JasonParse(text);
        return new JasonData(jp.list, jp.map);
    }


    public static void main(String[] args) {
        JasonParse jp = new JasonParse(" a  :{ 123 1234{123 456 }}  b : 2 ");
        System.out.println(jp.getMap());
    }


}
