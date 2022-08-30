package pl.futurecollars.invoicing.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import pl.futurecollars.invoicing.db.file.FilesService;

public class IdService {

  private final Path idFilePath;
  private final FilesService filesService;
  private int id = 1;

  IdService(Path idFilePath, FilesService filesService) {
    this.idFilePath = idFilePath;
    this.filesService = filesService;

    try {
      List<String> lines = filesService.readAllLines(idFilePath);
      if (lines.isEmpty()) {
        filesService.writeToFile(idFilePath, "1");
      } else {
        id = Integer.parseInt(lines.get(0));
      }
    } catch (IOException exception) {
      System.out.println("Creation of idFile failed");
      exception.printStackTrace();
    }
  }

  public int getNextIdAndIncrement() {
    try {
      Files.writeString(idFilePath, String.valueOf(id + 1));
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return id++;

  }
}

