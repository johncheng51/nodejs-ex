package com.jm.model;

import java.util.List;

public class PathModel extends AbstractModel 
{
    public PathModel(XmlBody xmlBody) 
    {   super(xmlBody);   }

    public List<PathField> list() {return list;}
}
