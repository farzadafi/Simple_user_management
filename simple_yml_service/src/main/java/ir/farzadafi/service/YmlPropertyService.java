package ir.farzadafi.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import ir.farzadafi.exception.DirectoryException;

@Service
public class YmlPropertyService {

  public List<String> getAllYmlFileNames() {
    List<File> files = getAllYmlFiles();
    return files.stream()
        .map(f -> f.getName())
        .toList();
  }

  private List<File> getAllYmlFiles() {
    File[] files = returnAllFileUnderResourcesDir();
    if (files.length == 0)
      throw new DirectoryException("localConfig directory is Empty!");
    List<File> ymlFiles = Arrays.stream(files)
        .filter(f -> isYmlFile(f))
        .collect(Collectors.toList());
    if (ymlFiles.isEmpty())
      throw new DirectoryException("yml file not found in resources directory");
    return ymlFiles;
  }

  public File[] returnAllFileUnderResourcesDir() {
    File[] files = null;
    try {
      files = ResourceUtils.getFile("classpath:").listFiles();
    } catch (IOException e) {
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

  public String ymlToJson(Object ymlData) {
    System.out.println(ymlData);
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(ymlData);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private File getYmlFileFromResourcesDir(String name) {
    File[] files = returnAllFileUnderResourcesDir();
    return Arrays.stream(files)
        .filter(f -> f.getName().equals(name))
        .findFirst()
        .orElseThrow(() -> new DirectoryException(String.format("%s not found!!", name)));
  }

  public String convertYmlToJson(String ymlName) {
    File file = getYmlFileFromResourcesDir(ymlName);
    Object object = getDataObjectFromYmlFile(file);
    return ymlToJson(object);
  }

  private Object getDataObjectFromYmlFile(File ymlFile) {
    Yaml yml = new Yaml();
    try(InputStream inputStream1 = new FileInputStream(ymlFile)) {
      return yml.load(inputStream1);
    }catch(IOException e){
      throw new YAMLException("property file not found!");
    }
  }
}
