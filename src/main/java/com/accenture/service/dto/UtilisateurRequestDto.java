package com.accenture.service.dto;

import jakarta.persistence.Id;

public record UtilisateurRequestDto(

   String login,
   String password,
   String nom,
   String prenom

) {
}
