package pl.futurecollars.invoicing.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import pl.futurecollars.invoicing.db.file.FilesService;

public class IdService {

  private final Path idFilePath;
  private final FilesService filesService;
  private int id = 1;

  IdService(Path idFilePath, FilesService filesService) {
    this.idFilePath = idFilePath;
    this.filesService = filesService;

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

  public int setId() {
    try {
      Files.writeString(idFilePath, String.valueOf(getId() + 1));
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return id++;

  }
}

