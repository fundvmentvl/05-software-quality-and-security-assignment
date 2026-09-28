package edu.seminolestate.tickets.service;
import edu.seminolestate.tickets.model.Ticket;
import edu.seminolestate.tickets.model.TicketStatus;
import edu.seminolestate.tickets.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class TicketService {
 private static final Logger log =
         LoggerFactory.getLogger(TicketService.class);
 private final TicketRepository repo;
 public TicketService(TicketRepository repo) {
  this.repo = repo;
 }
 public List<Ticket> all() {
  return repo.findAll();
 }
 public List<Ticket> search(String q) {
  return repo.search(q == null ? "" : q);
 }
 public Ticket get(long id) {
  return repo.findById(id)
          .orElseThrow(() ->
                  new IllegalArgumentException(
                          "No ticket with id " + id));
 }

 public Ticket getAuthorized(long id, String accessToken) {
  Ticket ticket = get(id);
  if (accessToken == null
          || accessToken.isBlank()
          || !ticket.accessToken().equals(accessToken)) {
   throw new SecurityException("Unauthorized ticket access");
  }
  return ticket;
 }

 public Ticket create(
         String name,
         String email,
         String category,
         String description) {
  String token = UUID.randomUUID().toString();
  validateTicketInput(name, email, category, description);
  Ticket ticket =
          repo.save(name, email, category, description, token);
  log.info("Created support ticket: {}", ticket.id());
  return ticket;
 }
 private void validateTicketInput(
         String name,
         String email,
         String category,
         String description) {
  if (name == null || name.isBlank()) {
   throw new IllegalArgumentException("Requester name is required");
  }
  if (email == null || email.isBlank() || !email.contains("@")) {
   throw new IllegalArgumentException("Valid email is required");
  }
  if (category == null || category.isBlank()) {
   throw new IllegalArgumentException("Category is required");
  }
  if (description == null || description.isBlank()) {
   throw new IllegalArgumentException("Description is required");
  }
  if (name.length() > 100
          || email.length() > 254
          || category.length() > 100
          || description.length() > 1000) {
   throw new IllegalArgumentException("Ticket input exceeds allowed length");
  }
 }

 public void changeStatus(
         long id,
         String status,
         String accessToken) {
  getAuthorized(id, accessToken);
  repo.updateStatus(
          id,
          TicketStatus.valueOf(status));
 }
}
