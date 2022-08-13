package pl.futurecollars.invoicing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.memory.InMemoryDatabase;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.Vat;
import pl.futurecollars.invoicing.service.InvoiceService;

public class App {

  public static void main(String[] args) {

    Database db = new InMemoryDatabase();
    InvoiceService service = new InvoiceService(db);

    Company buyer = new Company("123456789", "u200 Industrial Ave, Long Beach, CA 90803", "Stark Industries");
    Company seller = new Company("23233223", "12100 Coors Rd SW, Albuquerque, New Mexico 87045", "Los Pollos Hermanos");

    List<InvoiceEntry> products = List.of(new InvoiceEntry("Building robots", BigDecimal.valueOf(10000), BigDecimal.valueOf(2300), Vat.VAT_23));

    Invoice invoice = new Invoice(LocalDate.now(), buyer, seller, products);

    int id = service.save(invoice);

    service.getById(id).ifPresent(System.out::println);

    System.out.println(service.getAll());

    service.delete(id);
  }
}
