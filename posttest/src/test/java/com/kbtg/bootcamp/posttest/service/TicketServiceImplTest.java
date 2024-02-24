package com.kbtg.bootcamp.posttest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.kbtg.bootcamp.posttest.entity.Ticket;
import com.kbtg.bootcamp.posttest.entity.UserTicket;
import com.kbtg.bootcamp.posttest.exception.StatusInternalServerErrorException;
import com.kbtg.bootcamp.posttest.payload.request.TicketRequest;
import com.kbtg.bootcamp.posttest.payload.response.TicketResponse;
import com.kbtg.bootcamp.posttest.payload.response.TicketsResponse;
import com.kbtg.bootcamp.posttest.payload.response.UserTicketIdResponse;
import com.kbtg.bootcamp.posttest.payload.response.UserTicketsResponse;
import com.kbtg.bootcamp.posttest.repository.TicketRepository;
import com.kbtg.bootcamp.posttest.repository.UserTicketRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock private TicketRepository ticketRepository;

    @Mock private UserTicketRepository userTicketRepository;

    @InjectMocks private TicketServiceImpl ticketService;

    private Ticket ticket;

    private TicketRequest ticketRequest;

    private TicketResponse ticketResponse;

    @BeforeEach
    public void setup() {

        ticket = Ticket.builder().ticketId("123456").price(80).amount(1).build();

        ticketRequest = TicketRequest.builder().ticket("123456").price(80).amount(1).build();

        ticketResponse = TicketResponse.builder().ticket("123456").build();
    }

    // Unit test for registerTicket method
    @Test
    @DisplayName("Test for the registerTicket method should return a TicketResponse")
    public void givenTicketRequestObject_whenRegisterTicket_thenReturnTicketResponse() {

        // given - precondition or setup
        when(ticketRepository.findByTicketId(ticketRequest.getTicket()))
                .thenReturn(Optional.empty());

        when(ticketRepository.save(ticket)).thenReturn(ticket);

        // when - action or the behaviour that we are going to test
        TicketResponse actual = ticketService.registerTicket(ticketRequest);

        // then verify
        assertEquals(actual, ticketResponse);
    }

    // Unit test for registerTicket method
    @Test
    @DisplayName(
            "Test for the registerTicket method should throws an StatusInternalServerErrorException")
    public void givenExistingTicket_whenRegisterTicket_thenThrowsException() {

        // given - precondition or setup
        when(ticketRepository.findByTicketId(ticketRequest.getTicket()))
                .thenReturn(Optional.of(ticket));

        // when - action or the behaviour that we are going to test
        assertThrows(
                StatusInternalServerErrorException.class,
                () -> {
                    ticketService.registerTicket(ticketRequest);
                });

        // then verify
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    // Unit test for listAvailableTickets method
    @Test
    @DisplayName("Test for the listAvailableTickets method should return a TicketsResponse")
    public void whenListAvailableTickets_thenReturnTicketsResponse() {

        // given - precondition or setup
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket1 = Ticket.builder().ticketId("123456").price(80).amount(1).build();
        Ticket ticket2 = Ticket.builder().ticketId("234567").price(80).amount(1).build();
        Ticket ticket3 = Ticket.builder().ticketId("345678").price(80).amount(1).build();
        tickets.add(ticket1);
        tickets.add(ticket2);
        tickets.add(ticket3);

        when(ticketRepository.findByAmountGreaterThanEqual(1)).thenReturn(tickets);

        // when - action or the behaviour that we are going to test
        TicketsResponse actual = ticketService.listAvailableTickets();

        // then verify
        assertThat(actual).isNotNull();
        assertThat(actual.getTickets().size()).isEqualTo(3);
        assertThat(actual.getTickets().get(0)).isEqualTo("123456");
    }

    // Unit test for listAvailableTickets method
    @Test
    @DisplayName("Test for the listAvailableTickets method should return an empty TicketsResponse")
    public void whenNoListAvailableTickets_thenReturnEmptyTicketsResponse() {

        // given - precondition or setup
        List<Ticket> tickets = new ArrayList<>();
        when(ticketRepository.findByAmountGreaterThanEqual(1)).thenReturn(tickets);

        // when - action or the behaviour that we are going to test
        TicketsResponse actual = ticketService.listAvailableTickets();

        // then verify
        TicketsResponse expected = TicketsResponse.builder().tickets(new ArrayList<>()).build();
        assertThat(actual).isEqualTo(expected);
    }

    // Unit test for buyTicket method
    @Test
    @DisplayName("Test for the buyTicket method should return a UserTicketIdResponse")
    public void givenUserIdAndTicketId_whenBuyTicket_thenReturnUserTicketIdResponse() {

        // given - precondition or setup
        String userId = "0123456789";
        String ticketId = "123456";

        Ticket selectedTicket = Ticket.builder().ticketId("123456").price(80).amount(1).build();
        Ticket soldTicket = Ticket.builder().ticketId("123456").price(80).amount(0).build();
        UserTicket saveUserTicket =
                UserTicket.builder().id(1).userId("01234567890").ticket(selectedTicket).build();

        when(ticketRepository.findByTicketIdAndAmountGreaterThanEqual(ticketId, 1))
                .thenReturn(Optional.of(selectedTicket));

        when(userTicketRepository.save(any(UserTicket.class))).thenReturn(saveUserTicket);

        when(ticketRepository.save(soldTicket)).thenReturn(soldTicket);

        // when - action or the behaviour that we are going to test
        UserTicketIdResponse actual = ticketService.buyTicket(userId, ticketId);

        // then verify
        assertThat(actual.getId()).isEqualTo(1);
    }

    // Unit test for buyTicket method
    @Test
    @DisplayName(
            "Test for the buyTicket method with an unavailable ticketId should throw a StatusInternalServerErrorException")
    public void givenUserIdAndTicketId_whenBuyTicket_thenThrowException() {

        // given - precondition or setup
        String userId = "0123456789";
        String ticketId = "123456";

        Ticket selectedTicket = Ticket.builder().ticketId("123456").price(80).amount(1).build();

        when(ticketRepository.findByTicketIdAndAmountGreaterThanEqual(ticketId, 1))
                .thenReturn(Optional.empty());

        // when - action or the behaviour that we are going to test
        assertThrows(
                StatusInternalServerErrorException.class,
                () -> {
                    ticketService.buyTicket(userId, ticketId);
                });

        // then verify
        verify(userTicketRepository, never()).save(any(UserTicket.class));
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    // Unit test for sellTicket method
    @Test
    @DisplayName("Test for the sellTicket method should return the ticketId")
    public void givenUserIdAndTicketId_whenSellTicket_thenReturnSoldTicketId() {

        // given - precondition or setup
        String userId = "0123456789";
        String ticketId = "123456";

        Ticket ticket1 = Ticket.builder().ticketId("123456").price(80).amount(1).build();

        UserTicket userTicket1 =
                UserTicket.builder().userId("0123456789").ticket(ticket1).id(1).build();
        UserTicket userTicket2 =
                UserTicket.builder().userId("0123456788").ticket(ticket1).id(2).build();
        UserTicket userTicket3 =
                UserTicket.builder().userId("0123456787").ticket(ticket1).id(3).build();

        List<UserTicket> userTicketList = new ArrayList<>();
        userTicketList.add(userTicket1);
        userTicketList.add(userTicket2);
        userTicketList.add(userTicket3);

        when(userTicketRepository.findByUserId(userId)).thenReturn(userTicketList);

        // when - action or the behaviour that we are going to test
        TicketResponse actual = ticketService.sellTicket(userId, ticketId);

        // then verify
        assertThat(actual.getTicket()).isEqualTo(ticket1.getTicketId());
        verify(userTicketRepository, times(1)).delete(userTicket1);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    // Unit test for sellTicket method
    @Test
    @DisplayName(
            "Test for the sellTicket method with a non-existent userId should throw a StatusInternalServerErrorException")
    public void givenNoneExistUserIdAndTicketId_whenSellTicket_thenThrowsException() {

        // given - precondition or setup
        String userId = "0123456789";
        String ticketId = "123456";

        List<UserTicket> emptyList = new ArrayList<>();

        when(userTicketRepository.findByUserId(userId)).thenReturn(emptyList);

        // when - action or the behaviour that we are going to test
        assertThrows(
                StatusInternalServerErrorException.class,
                () -> {
                    TicketResponse actual = ticketService.sellTicket(userId, ticketId);
                });

        // then verify
        verify(userTicketRepository, times(0)).delete(any(UserTicket.class));
        verify(ticketRepository, times(0)).save(any(Ticket.class));
    }

    // Unit test for sellTicket method
    @Test
    @DisplayName(
            "Test for the sellTicket method with a non-existent ticketId should throw a StatusInternalServerErrorException")
    public void givenUserIdAndNoneExistTicketId_whenSellTicket_thenReturnSoldTicketId() {

        // given - precondition or setup
        String userId = "0123456789";
        String ticketId = "999999";

        Ticket ticket1 = Ticket.builder().ticketId("123456").price(80).amount(1).build();
        Ticket ticket2 = Ticket.builder().ticketId("123450").price(80).amount(1).build();

        UserTicket userTicket1 =
                UserTicket.builder().userId("0123456789").ticket(ticket1).id(1).build();
        UserTicket userTicket2 =
                UserTicket.builder().userId("0123456788").ticket(ticket1).id(2).build();
        UserTicket userTicket3 =
                UserTicket.builder().userId("0123456787").ticket(ticket1).id(3).build();
        UserTicket userTicket4 =
                UserTicket.builder().userId("0123456789").ticket(ticket2).id(3).build();

        List<UserTicket> userTicketList = new ArrayList<>();
        userTicketList.add(userTicket1);
        userTicketList.add(userTicket2);
        userTicketList.add(userTicket3);
        userTicketList.add(userTicket4);

        when(userTicketRepository.findByUserId(userId)).thenReturn(userTicketList);

        // when - action or the behaviour that we are going to test
        assertThrows(
                StatusInternalServerErrorException.class,
                () -> {
                    ticketService.sellTicket(userId, ticketId);
                });

        // then verify
        verify(userTicketRepository, times(0)).delete(any(UserTicket.class));
        verify(ticketRepository, times(0)).save(any(Ticket.class));
    }

    // Unit test for fetchUserTickets method
    @Test
    @DisplayName("Test for the fetchUserTickets method Should return a UserTicketsResponse")
    public void givenUserId_whenFetchUserTickets_shouldReturnUserTicketsResponseObject() {

        // given - precondition or setup
        String userId = "0123456789";

        Ticket ticket1 = Ticket.builder().ticketId("123456").price(80).amount(1).build();
        Ticket ticket2 = Ticket.builder().ticketId("123455").price(80).amount(1).build();
        Ticket ticket3 = Ticket.builder().ticketId("123454").price(80).amount(1).build();
        Ticket ticket4 = Ticket.builder().ticketId("123453").price(80).amount(1).build();

        UserTicket userTicket1 =
                UserTicket.builder().userId("0123456789").ticket(ticket1).id(1).build();
        UserTicket userTicket2 =
                UserTicket.builder().userId("0123456789").ticket(ticket2).id(2).build();
        UserTicket userTicket3 =
                UserTicket.builder().userId("0123456789").ticket(ticket3).id(3).build();
        UserTicket userTicket4 =
                UserTicket.builder().userId("0123456789").ticket(ticket4).id(3).build();

        List<UserTicket> userTicketList = new ArrayList<>();
        userTicketList.add(userTicket1);
        userTicketList.add(userTicket2);
        userTicketList.add(userTicket3);
        userTicketList.add(userTicket4);

        when(userTicketRepository.findByUserId(userId)).thenReturn(userTicketList);

        // when - action or the behaviour that we are going to test
        UserTicketsResponse actual = ticketService.fetchUserTickets(userId);

        // then verify
        assertThat(actual.getTickets().size()).isEqualTo(4);
        assertThat(actual.getTickets().get(0)).isEqualTo("123456");
        assertThat(actual.getTickets().get(1)).isEqualTo("123455");
        assertThat(actual.getTickets().get(2)).isEqualTo("123454");
        assertThat(actual.getTickets().get(3)).isEqualTo("123453");
        assertThat(actual.getCost()).isEqualTo(320);
        assertThat(actual.getCount()).isEqualTo(4);
    }

    // Unit test for fetchUserTickets method
    @Test
    @DisplayName("Test for the fetchUserTickets method should return an empty UserTicketsResponse")
    public void
            givenNonExistUserId_whenFetchUserTickets_shouldReturnEmptyUserTicketsResponseObject() {

        // given - precondition or setup
        String userId = "0123456789";
        List<UserTicket> userTicketList = new ArrayList<>();

        when(userTicketRepository.findByUserId(userId)).thenReturn(userTicketList);

        // when - action or the behaviour that we are going to test
        UserTicketsResponse actual = ticketService.fetchUserTickets(userId);

        // then verify
        assertThat(actual.getTickets()).isNullOrEmpty();
        assertThat(actual.getCost()).isEqualTo(0);
        assertThat(actual.getCount()).isEqualTo(0);
    }
}
