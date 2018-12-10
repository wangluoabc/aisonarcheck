package com.ai.total.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author songqian
 * @Created 2015年七�?21日下�?2:48.
 */

public class FileUtil {
    //读文件，返回字符�?
    public static  String ReadFile(String path){
//        System.out.println("------------"+path);
        File file = new File(path);
        BufferedReader reader = null;
        String laststr = "";
        try {
            //System.out.println("以行为单位读取文件内容，�?次读�?整行�?");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            //�?次读入一行，直到读入null为文件结�?
            while ((tempString = reader.readLine()) != null) {
                //显示行号
//               System.out.println("line "+ line +": " +tempString);
                laststr = laststr +tempString;

            }
            reader.close();
        } catch (IOException e) {
            System.out.println("AnalysisHtml--------"+ e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    System.out.println("AnalysisHtml--------"+ e1);
                }
            }
        }
        return laststr;
    }
    
}
