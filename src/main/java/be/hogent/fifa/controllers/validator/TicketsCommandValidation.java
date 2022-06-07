package be.hogent.fifa.controllers.validator;

import be.hogent.fifa.controllers.TicketsCommand;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class TicketsCommandValidation implements Validator {

    @Override
    public boolean supports(Class<?> klass) {
        return TicketsCommand.class.isAssignableFrom(klass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TicketsCommand cmd = (TicketsCommand) target;

        if (cmd.getVoetbalCode1() == null) {
            errors.rejectValue("voetbalCode1",
                    "voetbalCode1.null",
                    "moet ingevuld zijn");
        }
        if (cmd.getVoetbalCode2() == null) {
            errors.rejectValue("voetbalCode2",
                    "voetbalCode2.null",
                    "moet ingevuld zijn");
        }
        if (cmd.getAantal() == null) {
            errors.rejectValue("aantal",
                    "aantal.null",
                    "moet ingevuld zijn");
        }
        if (cmd.getEmail() == null) {
            errors.rejectValue("email",
                    "email.null",
                    "moet ingevuld zijn");
        }
        if (cmd.getVoetbalCode2() == null) {
            errors.rejectValue("voetbalCode2",
                    "voetbalCode2.null",
                    "moet ingevuld zijn");
        }

        Integer voetbalCode1 = null;
        Integer voetbalCode2 = null;
        Integer aantal = null;

        try {
            voetbalCode1 = Integer.parseInt(cmd.getVoetbalCode1());
        } catch (NumberFormatException e) {
            errors.rejectValue("voetbalCode1",
                    "voetbalCode1.getal",
                    "moet uit getallen bestaan");
        }
        try {
            voetbalCode2 = Integer.parseInt(cmd.getVoetbalCode2());
        } catch (NumberFormatException e) {
            errors.rejectValue("voetbalCode2",
                    "voetbalCode2.getal",
                    "moet uit getallen bestaan");
        }
        try {
            aantal = Integer.parseInt(cmd.getAantal());
        } catch (NumberFormatException e) {
            errors.rejectValue("aantal",
                    "aantal.getal",
                    "moet uit getallen bestaan");
        }

        if ((voetbalCode1 != null && voetbalCode2 != null) && voetbalCode1 >= voetbalCode2) {
            errors.rejectValue("voetbalCode1",
                    "voetbalCode1.kleinerDan",
                    "voetbalCode1 moet kleiner zijn dan voetbalCode2");
        }
        if (aantal != null && aantal > 25) {
            errors.rejectValue("aantal",
                    "aantalTickets.groter",
                    "aantal tickets moet kleiner of gelijk zijn aan 25");
        }
        if (aantal != null && aantal < 1) {
            errors.rejectValue("aantal",
                    "aantalTickets.kleiner",
                    "aantal tickets moet groter of gelijk zijn aan 1");
        }
        if(!cmd.getEmail().contains("@")) {
            errors.rejectValue("email",
                    "email.at",
                    "geen geldig email");
        }
    }
}
