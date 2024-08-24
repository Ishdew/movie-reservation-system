package com.trs.movie_ticket_reservation_system.services;

import com.trs.movie_ticket_reservation_system.convertor.TheaterConvertor;
import com.trs.movie_ticket_reservation_system.entities.Theater;
import com.trs.movie_ticket_reservation_system.entities.TheaterSeat;
import com.trs.movie_ticket_reservation_system.enums.SeatType;
import com.trs.movie_ticket_reservation_system.exceptions.TheaterIsExist;
import com.trs.movie_ticket_reservation_system.exceptions.TheaterIsNotExist;
import com.trs.movie_ticket_reservation_system.repositories.TheaterRepository;
import com.trs.movie_ticket_reservation_system.request.TheaterSeatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trs.movie_ticket_reservation_system.request.TheaterRequest;

import java.util.List;

@Service
public class TheaterService {

    @Autowired
    private TheaterRepository theaterRepository;

    public String addTheater( TheaterRequest theaterRequest) throws TheaterIsExist {
        if (theaterRepository.findByAddress(theaterRequest.getAddress()) != null) {
            throw new TheaterIsExist();
        }

        Theater theater = TheaterConvertor.theaterDtoToTheater(theaterRequest);

        theaterRepository.save(theater);
        return "Theater has been saved Successfully";
    }

    public String addTheaterSeat(TheaterSeatRequest entryDto) throws TheaterIsNotExist {
        if (theaterRepository.findByAddress(entryDto.getAddress()) == null) {
            throw new TheaterIsNotExist();
        }

        Integer noOfSeatsInRow = entryDto.getNoOfSeatInRow();
        Integer noOfPremiumSeats = entryDto.getNoOfPremiumSeat();
        Integer noOfClassicSeat = entryDto.getNoOfClassicSeat();
        String address = entryDto.getAddress();

        Theater theater = theaterRepository.findByAddress(address);

        List<TheaterSeat> seatList = theater.getTheaterSeatList();

        int counter = 1;
        int fill = 0;
        char ch = 'A';

        for (int i = 1; i <= noOfClassicSeat; i++) {
            String seatNo = Integer.toString(counter) + ch;

            ch++;
            fill++;
            if (fill == noOfSeatsInRow) {
                fill = 0;
                counter++;
                ch = 'A';
            }

            TheaterSeat theaterSeat = new TheaterSeat();
            theaterSeat.setSeatNo(seatNo);
            theaterSeat.setSeatType(SeatType.CLASSIC);
            theaterSeat.setTheater(theater);
            seatList.add(theaterSeat);
        }

        for (int i = 1; i <= noOfPremiumSeats; i++) {
            String seatNo = Integer.toString(counter) + ch;

            ch++;
            fill++;
            if (fill == noOfSeatsInRow) {
                fill = 0;
                counter++;
                ch = 'A';
            }

            TheaterSeat theaterSeat = new TheaterSeat();
            theaterSeat.setSeatNo(seatNo);
            theaterSeat.setSeatType(SeatType.PREMIUM);
            theaterSeat.setTheater(theater);
            seatList.add(theaterSeat);
        }

        theaterRepository.save(theater);

        return "Theater Seats have been added successfully";
    }
}
