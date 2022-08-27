package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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
      invoice.setId(idService.getId());
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
  public void update(int id, Invoice updatedInvoice) {
    if (getById(id).isEmpty()) {
      throw new IllegalArgumentException("Invoice id: " + id + " doesn't exist");
    }
    updatedInvoice.setId(id);
    String updatedInvoiceAsString = jsonService.objectToString(updatedInvoice).trim();

    try {
      String invoicesAsString = Files.readAllLines(invoicesPath)
          .stream()
          .map(invoice -> updatingInvoice(invoice, id, updatedInvoiceAsString))
          .collect(Collectors.joining("\n"));
      Files.writeString(invoicesPath, invoicesAsString, StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException exception) {
      throw new RuntimeException("Updating invoice failed");
    }
  }

  private String updatingInvoice(String oldInvoiceAsString, int id, String updatedInvoiceAsString) {
    return containsId(oldInvoiceAsString, id) ? updatedInvoiceAsString : oldInvoiceAsString;
  }

  @Override
  public void delete(int id) {
    try {
      String reducedInvoices = Files.readAllLines(invoicesPath)
          .stream()
          .filter(line -> !containsId(line, id))
          .collect(Collectors.joining("\n"));

      Files.writeString(invoicesPath, reducedInvoices, StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException exception) {
      throw new RuntimeException("Deleting invoice failed");
    }
  }

  private boolean containsId(String line, long id) {
    return line.contains("\"id\":" + id + ",");

  }
}
