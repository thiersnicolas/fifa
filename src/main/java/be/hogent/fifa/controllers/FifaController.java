package be.hogent.fifa.controllers;

import be.hogent.fifa.controllers.validator.TicketsCommandValidation;
import be.hogent.fifa.domain.Stadion;
import be.hogent.fifa.services.VoetbalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/fifa")
public class FifaController {
    private final VoetbalService voetbalService;
    private final TicketsCommandValidation ticketsCommandValidation;

    public FifaController(VoetbalService voetbalService, TicketsCommandValidation ticketsCommandValidation) {
        this.voetbalService = voetbalService;
        this.ticketsCommandValidation = ticketsCommandValidation;
    }

    @GetMapping
    public String showHomePage(Model model, @RequestParam(required = false) Integer verkocht, @RequestParam(required = false) Boolean uitverkocht) {
        if (verkocht != null) model.addAttribute("verkocht", verkocht);
        if (uitverkocht != null) model.addAttribute("uitverkocht", uitverkocht);
        model.addAttribute("stadions", voetbalService.getStadionLijst().stream().map(Stadion::getNaam).collect(Collectors.toList()));
        model.addAttribute("stadionCommand", new StadionCommand());
        return "stadionForm";
    }

    @PostMapping
    public String toonWedstrijden(@ModelAttribute StadionCommand stadionCommand, Model model) {
        model.addAttribute("stadionNaam", stadionCommand.getStadionNaam());
        model.addAttribute("wedstrijden", voetbalService.getWedstrijdenVoorStadionNaam(stadionCommand.getStadionNaam()));
        return "wedstrijdenView";
    }

    @GetMapping("/{wedstrijdId}")
    public String getWedstrijd(@PathVariable String wedstrijdId, Model model) {
        model.addAttribute("wedstrijd", voetbalService.getWedstrijd(UUID.fromString(wedstrijdId)));
        model.addAttribute("ticketsCommand", new TicketsCommand());
        return "wedstrijdView";
    }

    @PostMapping("/{wedstrijdId}/tickets")
    public String bestelTickets(@PathVariable String wedstrijdId, @ModelAttribute TicketsCommand ticketsCommand, BindingResult result, Model model) {
        ticketsCommandValidation.validate(ticketsCommand, result);

        if (result.hasErrors()) {
            model.addAttribute("wedstrijd", voetbalService.getWedstrijd(UUID.fromString(wedstrijdId)));
            //model.addAttribute("ticketsCommand", new TicketsCommand());
            return "wedstrijdView";
        }

        int verkochteTickets = voetbalService.ticketsBestellen(
                ticketsCommand.naarTickets(
                        voetbalService.getWedstrijd(UUID.fromString(wedstrijdId))
                )
        ).size();
        return "redirect:/fifa?verkocht=" + verkochteTickets;
    }
}
