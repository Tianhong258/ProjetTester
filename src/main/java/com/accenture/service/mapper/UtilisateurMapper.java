package com.accenture.service.mapper;

import com.accenture.repository.entity.Tache;
import com.accenture.repository.entity.Utilisateur;
import com.accenture.service.dto.TacheRequestDto;
import com.accenture.service.dto.TacheResponseDto;
import com.accenture.service.dto.UtilisateurRequestDto;
import com.accenture.service.dto.UtilisateurResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")//pour dire que spring va gérer cette classe en utilisant @Component
public interface UtilisateurMapper {

    Utilisateur toUtilisateur(UtilisateurRequestDto utilisateurRequestDto);
    UtilisateurResponseDto toUtilisateurResponseDto (Utilisateur utilisateur);
}
