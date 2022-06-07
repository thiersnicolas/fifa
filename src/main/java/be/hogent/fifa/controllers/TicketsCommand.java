package be.hogent.fifa.controllers;

import be.hogent.fifa.domain.Wedstrijd;
import be.hogent.fifa.domain.WedstrijdTicket;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicketsCommand {
    @NotEmpty
    private String email;
    @NumberFormat
    @NotEmpty
    private String aantal;
    @NumberFormat
    @NotEmpty
    private String voetbalCode1;
    @NumberFormat
    @NotEmpty
    private String voetbalCode2;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAantal() {
        return aantal;
    }

    public void setAantal(String aantal) {
        this.aantal = aantal;
    }

    public String getVoetbalCode1() {
        return voetbalCode1;
    }

    public void setVoetbalCode1(String voetbalCode1) {
        this.voetbalCode1 = voetbalCode1;
    }

    public String getVoetbalCode2() {
        return voetbalCode2;
    }

    public void setVoetbalCode2(String voetbalCode2) {
        this.voetbalCode2 = voetbalCode2;
    }

    public List<WedstrijdTicket> naarTickets(Wedstrijd wedstrijd) {
        return IntStream.range(0, Integer.parseInt(aantal))
                .mapToObj(i -> new WedstrijdTicket(email, Integer.parseInt(voetbalCode1), Integer.parseInt(voetbalCode2), wedstrijd))
                .collect(Collectors.toList());
    }
}
