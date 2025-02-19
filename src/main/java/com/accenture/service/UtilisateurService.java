package com.accenture.service;

import com.accenture.repository.entity.Utilisateur;
import com.accenture.service.dto.UtilisateurRequestDto;
import com.accenture.service.dto.UtilisateurResponseDto;

import java.util.List;

public interface UtilisateurService {
    UtilisateurResponseDto ajouter(UtilisateurRequestDto u);
    List<UtilisateurResponseDto> liste();


}
