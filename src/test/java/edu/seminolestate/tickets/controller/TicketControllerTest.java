package edu.seminolestate.tickets.controller;
import edu.seminolestate.tickets.service.TicketService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
class TicketControllerTest {
    @Test
    void errorDoesNotExposeExceptionDetails() {
        TicketService service = mock(TicketService.class);
        TicketController controller = new TicketController(service);
        Exception exception =
                new SecurityException("Unauthorized ticket access");
        String response = controller.error(exception);
        assertEquals(
                "An unexpected error occurred. Please try again.",
                response
        );
    }
}