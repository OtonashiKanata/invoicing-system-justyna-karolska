package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FilesService {

  void appendLineToFile(Path path, String line) throws IOException {
    Files.write(path, (line + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
  }

  void writeToFile(Path path, String line) throws IOException {
    Files.write(path, line.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
  }

  void writeLinesToFile(Path path, List<String> lines) throws IOException {
    Files.write(path, lines, StandardOpenOption.TRUNCATE_EXISTING);
  }

  List<String> readAllLines(Path path) throws IOException {
    return Files.readAllLines(path, StandardCharsets.ISO_8859_1);
  }
}
