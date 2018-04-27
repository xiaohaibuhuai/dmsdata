package com.illumi.dms.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {
    
    /** 
     * 将多个Excel打包成zip文件 
     * @param srcfile 
     * @param zipfile 
     */  
    public static void zipFiles(List<File> srcfile, File zipfile) {    
        byte[] buf = new byte[1024];    
        try {    
            // Create the ZIP file    
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));    
            // Compress the files    
            for (int i = 0; i < srcfile.size(); i++) {    
                File file = srcfile.get(i);    
                FileInputStream in = new FileInputStream(file);    
                // Add ZIP entry to output stream.    
                out.putNextEntry(new ZipEntry(file.getName()));    
                // Transfer bytes from the file to the ZIP file    
                int len;    
                while ((len = in.read(buf)) > 0) {    
                    out.write(buf, 0, len);    
                }    
                // Complete the entry    
                out.closeEntry();    
                in.close();    
            }    
            // Complete the ZIP file    
            out.close();   
        } catch (IOException e) {    
           e.printStackTrace();  
        }    
    }

}
