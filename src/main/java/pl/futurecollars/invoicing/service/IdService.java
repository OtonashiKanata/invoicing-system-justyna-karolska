package pl.futurecollars.invoicing.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IdService {

  private static int id = 1;
  private final Path idFilePath;

  public IdService(Path idFilePath) {
    this.idFilePath = idFilePath;
    try {
      File idFile = new File(String.valueOf(idFilePath));
      if (!idFile.exists() || Files.readString(Paths.get(String.valueOf(idFilePath))).isEmpty()) {
        idFile.createNewFile();
        Files.writeString(idFilePath, String.valueOf(id));
      }
    } catch (IOException exception) {
      System.out.println("Creation of idFile failed");
      exception.printStackTrace();
    }
  }

  public int getId() {
    try {
      String actualId = Files.readString(idFilePath);
      return Integer.parseInt(actualId);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return id;
  }

  public void setId() {
    try {
      Files.writeString(idFilePath, String.valueOf(getId() + 1));
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }
}
