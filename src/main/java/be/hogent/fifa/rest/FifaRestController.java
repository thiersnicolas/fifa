package be.hogent.fifa.rest;

import be.hogent.fifa.rest.dto.WedstrijdDto;
import be.hogent.fifa.services.VoetbalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fifaDetail")
public class FifaRestController {
    private final VoetbalService voetbalService;

    public FifaRestController(VoetbalService voetbalService) {
        this.voetbalService = voetbalService;
    }

    @GetMapping
    @ResponseBody
    public List<WedstrijdDto> getWedstrijden() {
        return voetbalService.getWedstrijdLijst().stream()
                .map(WedstrijdDto::fromWedstrijd)
                .collect(Collectors.toList());
    }

    @GetMapping("/{wedstrijdId}")
    public List<String> getWedstrijdLanden(@PathVariable String wedstrijdId) {
        return voetbalService.getWedstrijd(UUID.fromString(wedstrijdId)).getLanden();
    }
}
