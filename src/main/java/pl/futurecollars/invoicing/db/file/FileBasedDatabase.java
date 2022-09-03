package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.service.JsonService;

@AllArgsConstructor
public class FileBasedDatabase implements Database {

  private final Path invoicesPath;
  private final IdService idService;
  private final FilesService filesService;
  private final JsonService jsonService;

  @Override
  public int save(Invoice invoice) {
    try {
      invoice.setId(idService.getNextIdAndIncrement());
      filesService.appendLineToFile(invoicesPath, jsonService.objectToString(invoice));

      return invoice.getId();
    } catch (IOException exception) {
      throw new RuntimeException("Saving invoice to database failed", exception);
    }
  }

  @Override
  public Optional<Invoice> getById(int id) {
    try {
      return filesService.readAllLines(invoicesPath)
          .stream()
          .filter(line -> containsId(line, id))
          .map(line -> jsonService.stringToObject(line, Invoice.class))
          .findFirst();
    } catch (IOException exception) {
      throw new RuntimeException("Failed to get invoice with id: " + id, exception);

    }
  }

  @Override
  public List<Invoice> getAll() {
    try {
      return Files.readAllLines(invoicesPath)
          .stream()
          .map(line -> jsonService.stringToObject(line, Invoice.class))
          .collect(Collectors.toList());
    } catch (IOException exception) {
      throw new RuntimeException("Getting all invoices from database failed", exception);
    }
  }

  @Override
  public Optional<Invoice> update(int id, Invoice data) {
    Optional<Invoice> toUpdate = getById(id);
    try {
      List<String> allLines = filesService.readAllLines(invoicesPath);
      String invoiceToUpdateAsJson = allLines
          .stream()
          .filter(line -> containsId(line, id))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("Invoice with id: " + id + " could not be found"));

      allLines.remove(invoiceToUpdateAsJson);
      Invoice invoiceToUpdate = jsonService.stringToObject(invoiceToUpdateAsJson, Invoice.class);

      invoiceToUpdate.setDate(data.getDate());
      invoiceToUpdate.setBuyer(data.getBuyer());
      invoiceToUpdate.setSeller(data.getSeller());
      invoiceToUpdate.setEntries(data.getEntries());

      String updatedInvoiceAsJson = jsonService.objectToString(invoiceToUpdate);
      allLines.add(updatedInvoiceAsJson);
      filesService.writeLinesToFile(invoicesPath, allLines);

      return toUpdate;
    } catch (IOException exception) {
      throw new RuntimeException("Updating invoice failed for id: " + id, exception);
    }

  }

  @Override
  public Optional<Invoice> delete(int id) {
    Optional<Invoice> toDelete = getById(id);
    try {
      var updatedList = filesService.readAllLines(invoicesPath)
          .stream()
          .filter(line -> !containsId(line, id))
          .collect(Collectors.toList());

      filesService.writeLinesToFile(invoicesPath, updatedList);

      return toDelete;
    } catch (IOException exception) {
      throw new RuntimeException("Deleting invoice failed");
    }
  }

  private boolean containsId(String line, int id) {
    return line.contains("\"id\":" + id + ",");

  }
}
