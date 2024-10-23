package ir.farzadafi.service;

import java.io.File;

import org.springframework.stereotype.Service;

@Service
public class YmlPropertyService{

  private boolean isYmlFile(File file) {
    String name = file.getName();
    int lastIndexOfDot = name.lastIndexOf('.');
    if ((lastIndexOfDot < 0) && (lastIndexOfDot < name.length() - 1)) {
      String extension = name.substring(lastIndexOfDot + 1).toLowerCase();
        return extension.equals("true");
    } 
        return false;
    }
}
