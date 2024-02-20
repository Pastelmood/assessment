package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.entity.Ticket;
import com.kbtg.bootcamp.posttest.entity.UserTicket;
import com.kbtg.bootcamp.posttest.exception.StatusInternalServerErrorException;
import com.kbtg.bootcamp.posttest.payload.request.TicketRequest;
import com.kbtg.bootcamp.posttest.payload.response.TicketResponse;
import com.kbtg.bootcamp.posttest.payload.response.TicketsResponse;
import com.kbtg.bootcamp.posttest.payload.response.UserTicketIdResponse;
import com.kbtg.bootcamp.posttest.repository.TicketRepository;
import com.kbtg.bootcamp.posttest.repository.UserTicketRepository;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserTicketRepository userTicketRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private Ticket ticket;


    private TicketRequest ticketRequest;

    private TicketResponse ticketResponse;

    @BeforeEach
    public void setup() {

        ticket = Ticket.builder()
                .ticketId("123456")
                .price(80)
                .amount(1)
                .build();


        ticketRequest = TicketRequest.builder()
                .ticket("123456")
                .price(80)
                .amount(1)
                .build();

        ticketResponse = TicketResponse.builder()
                .ticket("123456")
                .build();
    }

    // Unit test for registerTicket method
    @Test
    @DisplayName("JUnit test for registerTicket method")
    public void givenTicketRequestObject_whenRegisterTicket_thenReturnTicketResponse() {

        // given - precondition or setup
        given(ticketRepository.findByTicketId(ticketRequest.getTicket()))
                .willReturn(Optional.empty());

        given(ticketRepository.save(ticket)).willReturn(ticket);

        System.out.println(ticketRepository);
        System.out.println(ticketService);

        // when - action or the behaviour that we are going to test
        TicketResponse actual = ticketService.registerTicket(ticketRequest);

        System.out.println(actual);

        // then verify
        assertEquals(actual, ticketResponse);
    }

    // Unit test for registerTicket method
    @Test
    @DisplayName("JUnit test for registerTicket method which throws exception")
    public void givenExistingTicket_whenRegisterTicket_thenThrowsException() {

        // given - precondition or setup
        given(ticketRepository.findByTicketId(ticketRequest.getTicket()))
                .willReturn(Optional.of(ticket));

        System.out.println(ticketRepository);
        System.out.println(ticketService);

        // when - action or the behaviour that we are going to test
        assertThrows(StatusInternalServerErrorException.class, () -> {
            ticketService.registerTicket(ticketRequest);
        });

        // then verify
        verify(ticketRepository, never()).save(any(Ticket.class));

    }

    // Unit test for listAvailableTickets method
    @Test
    @DisplayName("JUnit test for listAvailableTickets method")
    public void whenListAvailableTickets_thenReturnTicketsResponse() {

        // given - precondition or setup
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket1 = Ticket.builder().ticketId("123456").price(80).amount(1).build();
        Ticket ticket2 = Ticket.builder().ticketId("234567").price(80).amount(1).build();
        Ticket ticket3 = Ticket.builder().ticketId("345678").price(80).amount(1).build();
        tickets.add(ticket1);
        tickets.add(ticket2);
        tickets.add(ticket3);

        given(ticketRepository.findByAmountGreaterThanEqual(1)).willReturn(tickets);

        // when - action or the behaviour that we are going to test
        TicketsResponse actual = ticketService.listAvailableTickets();

        // then verify
        assertThat(actual).isNotNull();
        assertThat(actual.getTickets().size()).isEqualTo(3);
        assertThat(actual.getTickets().get(0)).isEqualTo("123456");

    }

    // Unit test for listAvailableTickets method
    @Test
    @DisplayName("JUnit test for listAvailableTickets method with empty response")
    public void whenListAvailableTickets_thenReturnEmptyObject() {

        // given - precondition or setup
        List<Ticket> tickets = new ArrayList<>();
        given(ticketRepository.findByAmountGreaterThanEqual(1)).willReturn(tickets);

        // when - action or the behaviour that we are going to test
        TicketsResponse actual = ticketService.listAvailableTickets();

        System.out.println(actual);

        // then verify
        TicketsResponse expected = new TicketsResponse();
        assertThat(actual).isEqualTo(expected);

    }

    // Unit test for buyTicket method
    @Test
    @DisplayName("JUnit test for buyTicket method")
    public void givenUserIdAndTicketId_whenBuyTicket_thenReturnUserTicketIdResponse() {

        // given - precondition or setup
        String userId = "0123456789";
        String ticketId = "123456";

        Ticket selectedTicket = Ticket.builder().ticketId("123456").price(80).amount(1).build();

        Ticket soldTicket = Ticket.builder().ticketId("123456").price(80).amount(0).build();

        UserTicket saveUserTicket = UserTicket.builder().id(1).userId("01234567890").ticket(selectedTicket).build();

        given(ticketRepository.findByTicketIdAndAmountGreaterThanEqual(ticketId, 1))
                .willReturn(Optional.of(selectedTicket));

        given(userTicketRepository.save(any(UserTicket.class)))
                .willReturn(saveUserTicket);

        given(ticketRepository.save(soldTicket))
                .willReturn(soldTicket);

        // when - action or the behaviour that we are going to test
        UserTicketIdResponse actual = ticketService.buyTicket(userId, ticketId);

        // then verify
        assertThat(actual.getId()).isEqualTo(1);
    }

    // Unit test for buyTicket method
    @Test
    @DisplayName("JUnit test for buyTicket method which throws exception")
    public void givenUserIdAndTicketId_whenBuyTicket_thenThrowException() {

        // given - precondition or setup
        String userId = "0123456789";
        String ticketId = "123456";

        Ticket selectedTicket = Ticket.builder().ticketId("123456").price(80).amount(1).build();

        given(ticketRepository.findByTicketIdAndAmountGreaterThanEqual(ticketId, 1))
                .willReturn(Optional.empty());

        // when - action or the behaviour that we are going to test
        assertThrows(StatusInternalServerErrorException.class, () -> {
            ticketService.buyTicket(userId, ticketId);
        });

        // then verify
        verify(userTicketRepository, never()).save(any(UserTicket.class));
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    // Unit test for sellTicket method
    @Test
    @DisplayName("JUnit test for sellTicket method")
    public void givenUserIdAndTicketId_whenSellTicket_thenReturnSoldTicketId() {

        // given - precondition or setup
        String userId = "0123456789";
        String ticketId = "123456";

        Ticket ticket1 = Ticket.builder().ticketId("123456").price(80).amount(1).build();

        UserTicket userTicket1 = UserTicket.builder().userId("0123456789").ticket(ticket1).id(1).build();
        UserTicket userTicket2 = UserTicket.builder().userId("0123456788").ticket(ticket1).id(2).build();
        UserTicket userTicket3 = UserTicket.builder().userId("0123456787").ticket(ticket1).id(3).build();

        List<UserTicket> userTicketList = new ArrayList<>();
        userTicketList.add(userTicket1);
        userTicketList.add(userTicket2);
        userTicketList.add(userTicket3);

        given(userTicketRepository.findByUserId(userId))
                .willReturn(userTicketList);

        // when - action or the behaviour that we are going to test
        TicketResponse actual = ticketService.sellTicket(userId, ticketId);

        // then verify
        assertThat(actual.getTicket()).isEqualTo(ticket1.getTicketId());
        verify(userTicketRepository, times(1)).delete(userTicket1);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    // Unit test for sellTicket method
    @Test
    @DisplayName("JUnit test for sellTicket method which throw exception from non-exist userId")
    public void givenUnExistUserIdAndTicketId_whenSellTicket_thenThrowsException() {

        // given - precondition or setup
        String userId = "0123456789";
        String ticketId = "123456";

        List<UserTicket> emptyList = new ArrayList<>();

        given(userTicketRepository.findByUserId(userId))
                .willReturn(emptyList);


        // when - action or the behaviour that we are going to test
        assertThrows(StatusInternalServerErrorException.class, () -> {
            TicketResponse actual = ticketService.sellTicket(userId, ticketId);
        });


        // then verify
        verify(userTicketRepository, times(0)).delete(any(UserTicket.class));
        verify(ticketRepository, times(0)).save(any(Ticket.class));
    }

    // Unit test for sellTicket method
    @Test
    @DisplayName("JUnit test for sellTicket method which throws exception from non-exist ticketId")
    public void givenUserIdAndNoneExistTicketId_whenSellTicket_thenReturnSoldTicketId() {

        // given - precondition or setup
        String userId = "0123456789";
        String ticketId = "999999";

        Ticket ticket1 = Ticket.builder().ticketId("123456").price(80).amount(1).build();
        Ticket ticket2 = Ticket.builder().ticketId("123450").price(80).amount(1).build();

        UserTicket userTicket1 = UserTicket.builder().userId("0123456789").ticket(ticket1).id(1).build();
        UserTicket userTicket2 = UserTicket.builder().userId("0123456788").ticket(ticket1).id(2).build();
        UserTicket userTicket3 = UserTicket.builder().userId("0123456787").ticket(ticket1).id(3).build();
        UserTicket userTicket4 = UserTicket.builder().userId("0123456789").ticket(ticket2).id(3).build();

        List<UserTicket> userTicketList = new ArrayList<>();
        userTicketList.add(userTicket1);
        userTicketList.add(userTicket2);
        userTicketList.add(userTicket3);
        userTicketList.add(userTicket4);

        given(userTicketRepository.findByUserId(userId))
                .willReturn(userTicketList);

        // when - action or the behaviour that we are going to test
        assertThrows(StatusInternalServerErrorException.class, () -> {
            ticketService.sellTicket(userId, ticketId);
        });

        // then verify
        verify(userTicketRepository, times(0)).delete(any(UserTicket.class));
        verify(ticketRepository, times(0)).save(any(Ticket.class));
    }

}