/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stam.batchmove;

import java.io.File;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author StamaterisG
 */
public class BatchMoveUtils {
    public static File findFileByName(List<File> files, String fileName) {
        File file = null;
        
        for (File f : files) {
            if(f.isFile()) {
                String fn = f.getName();
                String fileNameWithoutExt = FilenameUtils.removeExtension(fn);
                if (fileName.equals(fn)) {
                    file = f;
                    break;
                } else if (fileName.equals(fileNameWithoutExt)) {
                    file = f;
                    break;
                }
            }
        }
        
        return file;
    }
}
