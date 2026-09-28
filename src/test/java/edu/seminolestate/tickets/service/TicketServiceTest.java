package edu.seminolestate.tickets.service;
import edu.seminolestate.tickets.model.Ticket;
import edu.seminolestate.tickets.model.TicketStatus;
import edu.seminolestate.tickets.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class TicketServiceTest {
 @Mock
 TicketRepository repo;
 @InjectMocks
 TicketService service;
 @Test
 void retrievesExistingTicket() {
  var ticket = new Ticket(
          1L,
          "Jordan Lee",
          "jlee@example.edu",
          "Software",
          "Problem",
          TicketStatus.OPEN,
          "token"
  );
  when(repo.findById(1L))
          .thenReturn(Optional.of(ticket));
  assertEquals(ticket, service.get(1L));
 }
 @Test
 void changeStatusWithValidTokenUpdatesTicket() {
  var ticket = new Ticket(
          1L,
          "Jordan Lee",
          "jlee@example.edu",
          "Software",
          "Problem",
          TicketStatus.OPEN,
          "token"
  );
  when(repo.findById(1L))
          .thenReturn(Optional.of(ticket));
  service.changeStatus(
          1L,
          "IN_PROGRESS",
          "token"
  );
  verify(repo).updateStatus(
          1L,
          TicketStatus.IN_PROGRESS
  );
 }
 @Test
 void searchDelegatesToRepository() {
  when(repo.search("Wi-Fi"))
          .thenReturn(List.of(
                  new Ticket(
                          2L,
                          "Morgan Diaz",
                          "mdiaz@example.edu",
                          "Network",
                          "Cannot connect to campus Wi-Fi",
                          TicketStatus.IN_PROGRESS,
                          "token"
                  )
          ));
  assertEquals(
          1,
          service.search("Wi-Fi").size()
  );
 }
 @Test
 void rejectsIncorrectAccessToken() {
  var ticket = new Ticket(
          1L,
          "Jordan Lee",
          "jlee@example.edu",
          "Software",
          "Problem",
          TicketStatus.OPEN,
          "correct-token"
  );
  when(repo.findById(1L))
          .thenReturn(Optional.of(ticket));
  assertThrows(
          SecurityException.class,
          () -> service.getAuthorized(
                  1L,
                  "wrong-token"
          )
  );
 }
 @Test
 void incorrectTokenPreventsStatusUpdate() {
  var ticket = new Ticket(
          1L,
          "Jordan Lee",
          "jlee@example.edu",
          "Software",
          "Problem",
          TicketStatus.OPEN,
          "correct-token"
  );
  when(repo.findById(1L))
          .thenReturn(Optional.of(ticket));
  assertThrows(
          SecurityException.class,
          () -> service.changeStatus(
                  1L,
                  "CLOSED",
                  "wrong-token"
          )
  );
  verify(repo, never()).updateStatus(
          anyLong(),
          any(TicketStatus.class)
  );
 }
 @Test
 void createRejectsBlankRequesterName() {
  assertThrows(
          IllegalArgumentException.class,
          () -> service.create("", "test@example.com",
                  "Software", "Test description")
  );
 }
 @Test
 void createRejectsInvalidEmail() {
  assertThrows(
          IllegalArgumentException.class,
          () -> service.create("Jordan Lee", "invalid-email",
                  "Software", "Test description")
  );
 }
 @Test
 void createRejectsBlankDescription() {
  assertThrows(
          IllegalArgumentException.class,
          () -> service.create("Jordan Lee", "test@example.com",
                  "Software", "")
  );
 }
 @Test
 void createRejectsOversizedDescription() {
  String description = "A".repeat(1001);
  assertThrows(
          IllegalArgumentException.class,
          () -> service.create("Jordan Lee", "test@example.com",
                  "Software", description)
  );
 }
}