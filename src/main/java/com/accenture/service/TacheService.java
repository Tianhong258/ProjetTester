package com.accenture.service;

import com.accenture.exception.TacheException;
import com.accenture.model.Priorite;
import com.accenture.service.dto.TacheRequestDto;
import com.accenture.service.dto.TacheResponseDto;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface TacheService {
    TacheResponseDto trouver(int id) throws EntityNotFoundException;
    List<TacheResponseDto> trouverToutes();
    TacheResponseDto ajouter(TacheRequestDto tacheRequestDto) throws TacheException;

    TacheResponseDto modifier(int id, TacheRequestDto tacheRequestDto) throws TacheException;

    TacheResponseDto modifierPartiellement(int id, TacheRequestDto tacheRequestDto) throws TacheException;
    void supprimer(int id) throws TacheException;

    List<TacheResponseDto> rechercher(String libelle, LocalDate dateLimite, Priorite priorite, Boolean termine);
}
