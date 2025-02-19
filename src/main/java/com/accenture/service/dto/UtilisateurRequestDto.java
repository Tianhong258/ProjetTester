package com.accenture.service.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UtilisateurRequestDto(

   String login,

   @NotNull(message = "Le mot de passe est obligatoire")
   String password,
   String nom,
   String prenom

) {
}
