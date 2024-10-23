package ir.farzadafi.service;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import ir.farzadafi.exception.DirectoryException;

@Service
public class YmlPropertyService{

  public File[] returnAllFileUnderResourcesDir() {
    File[] files = null;
    try{
      files = ResourceUtils.getFile("classpath:").listFiles();
    }catch(IOException e) {
      throw new DirectoryException("find files under resources occur exception");
    }
    return files;
  }

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
