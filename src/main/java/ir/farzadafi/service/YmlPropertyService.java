package ir.farzadafi.service;

import java.io.File;
import java.util.List;

import ir.farzadafi.exception.DirectoryException;

public class YmlPropertyService{

  private String localConfigAddress;

  public List<String> getAllYmlFileNames() {
    File[] files = getAllFiles();
    return null;
  }

  private File[] getAllFiles() {
    File directory = new File(localConfigAddress);
    if (!directory.exists())
      throw new DirectoryException("localConfig directory Not Found!");
    if(!directory.isDirectory())
      throw new DirectoryException("localConfig Address is not a dirctory!");

    File[] listFiles = directory.listFiles();
    if(listFiles == null)
      throw new DirectoryException("Something is wrong");
    if(listFiles.length == 0)
      throw new DirectoryException("localConfig directory is Empty!");
    return listFiles;
  }
}
