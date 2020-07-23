package com.project.backend.Common;

import java.util.List;

public class CommonFunc {
    public static String sqlWhereInCreater(
        int size, 
        List<String> values,
        
        String name
    ) {
        StringBuilder strbuild = new StringBuilder();
        for(int i =0; i < values.size(); ++i) {
            strbuild.append(name + i + " ,");


        }    
        
        
        return null;
    
    
    }
}