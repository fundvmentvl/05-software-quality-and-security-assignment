package edu.seminolestate.tickets.repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
class TicketRepositoryTest {
    private JdbcTemplate jdbc;
    private TicketRepository repository;
    @BeforeEach
    void setUp() {
        jdbc = mock(JdbcTemplate.class);
        repository = new TicketRepository(jdbc);
    }
    @Test
    void searchUsesParameterizedQueryForNormalInput() {
        repository.search("printer");
        verify(jdbc).query(
                any(String.class),
                any(org.springframework.jdbc.core.RowMapper.class),
                eq("%printer%"),
                eq("%printer%")
        );
    }
    @Test
    void searchTreatsSqlInjectionAttemptAsParameterValue() {
        String maliciousInput = "' OR '1'='1";
        repository.search(maliciousInput);
        verify(jdbc).query(
                any(String.class),
                any(org.springframework.jdbc.core.RowMapper.class),
                eq("%' OR '1'='1%"),
                eq("%' OR '1'='1%")
        );
    }
}