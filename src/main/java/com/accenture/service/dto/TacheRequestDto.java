package com.accenture.service.dto;

import com.accenture.model.Priorite;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TacheRequestDto(
        @NotBlank(message = "Le libellé est obligatoire")
        String libelle,

        @NotNull(message = "La date limite est obligatoire")
        @FutureOrPresent(message = "La date limite doit postérieure ou égale à aujourd'hui")
        LocalDate dateLimite,

        @NotNull(message = "La niveau est obligatoire")
        Priorite niveau,

        @NotNull(message = "La terminé est obligatoire")
        Boolean termine
) {
}
