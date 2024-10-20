package ir.farzadafi.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import ir.farzadafi.exception.DirectoryException;

@Service
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

  @PostConstruct
  public void test() throws FileNotFoundException {
    System.out.println("farzad");
    System.out.println("farzad");
    System.out.println("farzad");
    System.out.println("farzad");
    System.out.println("farzad");
    System.out.println("farzad");
    System.out.println("farzad");
    System.out.println("farzad");
    File file = ResourceUtils.getFile("classpath:");
    File[] files = file.listFiles();
    for (File file2: files) {
          System.out.println(file2.getName()); 
    }
  }
}
