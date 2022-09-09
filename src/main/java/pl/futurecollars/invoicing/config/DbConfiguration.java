package pl.futurecollars.invoicing.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.context.annotation.Bean;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.file.FileBasedDatabase;
import pl.futurecollars.invoicing.db.file.FilesService;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.service.JsonService;

public class DbConfiguration {

  public static final String INVOICES_FILE_NAME = "invoices.txt";
  public static final String ID_FILE_NAME = "id.txt";
  public static final String DATABASE_LOCATION = "db";

  @Bean
  public IdService idService(FilesService filesService) throws IOException {
    Path idFilePath = Files.createTempFile(DATABASE_LOCATION, ID_FILE_NAME);
    return new IdService(idFilePath, filesService);
  }

  @Bean
  public Database fileBasedDatabase(IdService idService, FilesService filesService, JsonService jsonService) throws IOException {
    Path databaseFilePath = Files.createTempFile(DATABASE_LOCATION, INVOICES_FILE_NAME);
    return new FileBasedDatabase(databaseFilePath, idService, filesService, jsonService);
  }

}


