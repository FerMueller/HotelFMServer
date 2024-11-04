package com.hotelFM.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long guesId;
    private LocalDateTime dtStartBooking;
    private LocalDateTime dtEndBooking;
    private LocalDateTime dtCheckin;
    private LocalDateTime dtCheckout;
    private boolean hasCar;
    private double qtAccValue;

}
