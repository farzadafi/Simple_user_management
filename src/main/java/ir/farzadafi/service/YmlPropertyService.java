package ir.farzadafi.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import ir.farzadafi.exception.DirectoryException;

@Service
public class YmlPropertyService{

  private List<File> getAllYmlFiles() {
    File[] files = returnAllFileUnderResourcesDir();
    if(files.length == 0)
      throw new DirectoryException("localConfig directory is Empty!");
    List<File> ymlFiles = Arrays.stream(files)
      .filter(f -> isYmlFile(f))
      .collect(Collectors.toList());
    if(ymlFiles.isEmpty())
      throw new DirectoryException("yml file not found in resources directory");
    return ymlFiles;
  }

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
