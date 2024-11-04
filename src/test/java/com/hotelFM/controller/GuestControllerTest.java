package com.hotelFM.controller;

import com.hotelFM.model.Guest;
import com.hotelFM.service.GuestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GuestController.class)
public class GuestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GuestService guestService;

    @Test
    public void testListGuests() throws Exception {
        // Mocking the service response
        Guest h1 = new Guest();
        h1.setId(1L);
        h1.setNome("Fernando Mueller");
        h1.setDocumento("123456789");
        h1.setTelefone("47996395947");
        h1.setNoHotel(false);

        Guest h2 = new Guest();
        h2.setId(2L);
        h2.setNome("Maqueli Petri");
        h2.setDocumento("987654321");
        h2.setTelefone("47996395947");

        when(guestService.listAllGuest()).thenReturn(Arrays.asList(h1, h2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/guest")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Fernando Mueller"))
                .andExpect(jsonPath("$[1].nome").value("Maqueli Petri"));

        verify(guestService, times(1)).listAllGuest();
    }

    @Test
    public void testAddGuest() throws Exception {
        Guest guest = new Guest();
        guest.setId(1L);
        guest.setNome("Fernando Mueller");
        guest.setDocumento("05338189909");
        guest.setTelefone("47996395947");

        when(guestService.saveHospede(any(Guest.class))).thenReturn(guest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/guest")
                        .content("{\"nome\": \"Fernando Mueller\", \"documento\": \"05338189909\", \"telefone\": \"47996395947\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Fernando Mueller"))
                .andExpect(jsonPath("$.documento").value("05338189909"));

        verify(guestService, times(1)).saveHospede(any(Guest.class));
    }
}
