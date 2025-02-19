package com.accenture.controller;

import com.accenture.repository.UtilisateurDao;
import com.accenture.service.UtilisateurService;
import com.accenture.service.dto.UtilisateurRequestDto;
import com.accenture.service.dto.UtilisateurResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/utilisateurs")
public class UtilisateurController {
    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    List<UtilisateurResponseDto> tous(){
        return utilisateurService.liste();
    }

    @PostMapping
    ResponseEntity<Void> ajouter(@RequestBody UtilisateurRequestDto utilisateurRequestDto){
        utilisateurService.ajouter(utilisateurRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
