package com.hotelFM.controller;

import com.hotelFM.model.Guest;
import com.hotelFM.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guest")
@CrossOrigin(origins = "http://localhost:4200")
public class GuestController {

    @Autowired
    private GuestService guestService;

    @GetMapping
    public List<Guest> listAllGuest() {
        return guestService.listAllGuest();
    }

    @PostMapping
    public Guest createGuest(@RequestBody Guest guest) {
        return guestService.saveHospede(guest);
    }

    @PutMapping("/{id}")
    public Guest editGuest(@PathVariable Long id, @RequestBody Guest guest) {
        guest.setId(id);
        return guestService.saveHospede(guest);
    }

    @DeleteMapping("/{id}")
    public void deleteGuest(@PathVariable Long id) {
        guestService.deleteGuest(id);
    }

    @GetMapping("/find/name")
    public List<Guest> findbyName(@RequestParam String nome) {
        return guestService.findByName(nome);
    }

    @GetMapping("/find/document")
    public List<Guest> findbyDocument(@RequestParam String documento) {
        return guestService.findByDocument(documento);
    }

    @GetMapping("/find/phone")
    public List<Guest> findByPhone(@RequestParam String telefone) {
        return guestService.findByPhone(telefone);
    }

    @GetMapping("/in-hotel")
    public List<Guest> getGuestInHotel(@RequestParam Long guestId) {
        return guestService.findInHotel();
    }

    @GetMapping("/without-checkin")
    public List<Guest> getGuestWithoutCheckin() {
        return guestService.findByWithoutCheckin();
    }

}
