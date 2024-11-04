package com.hotelFM.service;

import com.hotelFM.model.Guest;
import com.hotelFM.repository.GuestRepository;
import com.hotelFM.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GuestServiceTest {

    @Autowired
    private GuestService guestService;

    @MockBean
    private GuestRepository guestRepository;

    @MockBean
    private BookingRepository reservaRepository;

    @Test
    public void testListAllGuest() {
        Guest guest1 = new Guest();
        guest1.setId(1L);
        guest1.setNome("João");

        Guest guest2 = new Guest();
        guest2.setId(2L);
        guest2.setNome("Maria");

        when(guestRepository.findAll()).thenReturn(Arrays.asList(guest1, guest2));

        List<Guest> guests = guestService.listAllGuest();

        assertEquals(2, guests.size());
        verify(guestRepository, times(1)).findAll();
    }

    @Test
    public void changeGuestStatus() {
        Guest guest = new Guest();
        guest.setId(1L);
        guest.setNome("Fernando");
        guest.setNoHotel(false);

        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        when(guestRepository.save(any(Guest.class))).thenReturn(guest);

        guestService.changeGuestStatus(1L, true);

        assertTrue(guest.isNoHotel());
        verify(guestRepository, times(1)).save(guest);
    }
}
