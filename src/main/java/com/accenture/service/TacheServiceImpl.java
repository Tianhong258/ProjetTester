package com.accenture.service;

import com.accenture.exception.TacheException;
import com.accenture.model.Priorite;
import com.accenture.repository.entity.Tache;
import com.accenture.repository.TacheDao;
import com.accenture.service.dto.TacheRequestDto;
import com.accenture.service.dto.TacheResponseDto;
import com.accenture.service.mapper.TacheMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TacheServiceImpl implements TacheService {

    public static final String ID_NON_PRESENT = "id non présent";
    private final TacheDao tacheDao;
    private final TacheMapper tacheMapper;

    public TacheServiceImpl(TacheDao tacheDao, TacheMapper tacheMapper) {
        this.tacheDao = tacheDao;
        this.tacheMapper = tacheMapper;
    }

    /************************************************************
     METHODES PUBLIQUES
     *************************************************************/
    @Override
    public TacheResponseDto trouver(int id) throws EntityNotFoundException {
        Optional<Tache> optTache = tacheDao.findById(id);
        if (optTache.isEmpty())
            throw new EntityNotFoundException(ID_NON_PRESENT);
        Tache tache = optTache.get();
        return tacheMapper.toTacheResponseDto(tache);
    }

    @Override
    public List<TacheResponseDto> trouverToutes() {
        return tacheDao.findAll().stream()
                .map(tacheMapper::toTacheResponseDto)
                .toList();
    }

    @Override
    public TacheResponseDto ajouter(TacheRequestDto tacheRequestDto) throws TacheException {
        verifierTache(tacheRequestDto);
        Tache tache = tacheMapper.toTache(tacheRequestDto);

        Tache tacheRetour = tacheDao.save(tache);
        return tacheMapper.toTacheResponseDto(tacheRetour);
    }


    @Override
    public TacheResponseDto modifier(int id, TacheRequestDto tacheRequestDto) throws TacheException, EntityNotFoundException {
        if (!tacheDao.existsById(id))
            throw new EntityNotFoundException(ID_NON_PRESENT);

        verifierTache(tacheRequestDto);

        Tache tache = tacheMapper.toTache(tacheRequestDto);
        tache.setId(id);

        Tache tacheEnreg = tacheDao.save(tache);
        return tacheMapper.toTacheResponseDto(tacheEnreg);
    }

    @Override
    public TacheResponseDto modifierPartiellement(int id, TacheRequestDto tacheRequestDto) throws TacheException, EntityNotFoundException {
        Optional<Tache> optTache = tacheDao.findById(id);
        if (optTache.isEmpty())
            throw new EntityNotFoundException(ID_NON_PRESENT);
        Tache tacheExistante = optTache.get();

        Tache nouvelle = tacheMapper.toTache(tacheRequestDto);

        remplacer(nouvelle, tacheExistante);

//        verifierTache(tacheExistante);
        Tache tacheEnreg = tacheDao.save(tacheExistante);
        return tacheMapper.toTacheResponseDto(tacheEnreg);

    }


    @Override
    public void supprimer(int id) throws EntityNotFoundException {
        if (tacheDao.existsById(id))
            tacheDao.deleteById(id);
        else
            throw new EntityNotFoundException(ID_NON_PRESENT);
    }

    @Override
    public List<TacheResponseDto> rechercher(String libelle, LocalDate dateLimite, Priorite priorite, Boolean termine) {
        List<Tache> liste = null;
//        if(libelle != null)
//            liste = tacheDao.findByLibelleContaining(libelle);
//        else if (dateLimite != null){
//            liste = tacheDao.findByDateLimite(dateLimite);
//        } else if (priorite != null) {
//            liste = tacheDao.findByNiveau(priorite);
//        } else if (termine != null) {
//            liste = tacheDao.findByTermine(termine);
//        }

//        int choix = (libelle != null) ? 1 : (dateLimite != null) ? 2 : (priorite != null) ? 3 : (termine != null) ? 4 : 0;
//        liste = switch(choix){
//            case 1 -> tacheDao.findByLibelleContaining(libelle);
//            case 2 -> tacheDao.findByDateLimite(dateLimite);
//            case 3 -> tacheDao.findByNiveau(priorite);
//            case 4 -> tacheDao.findByTermine(termine);
//            default -> throw new TacheException("Un critère de recherche est obligatoire");
//        };



        liste = Optional.ofNullable(libelle)
                .map(tacheDao::findByLibelleContaining)
                .or(() -> Optional.ofNullable(dateLimite).map(tacheDao::findByDateLimite))
                .or(() -> Optional.ofNullable(priorite).map(tacheDao::findByNiveau))
                .or(() -> Optional.ofNullable(termine).map(tacheDao::findByTermine))
                .orElseThrow(() -> new TacheException("Un critère de recherche est obligatoire"));

        return liste.stream()
                .map(tacheMapper::toTacheResponseDto)
                .toList();
    }

    /************************************************************
     METHODES PRIVEES
     *************************************************************/

    private static void remplacer(Tache tache, Tache tacheExistante) {
        if (tache.getTermine() != null)
            tacheExistante.setTermine(tache.getTermine());
        if (tache.getLibelle() != null)
            tacheExistante.setLibelle(tache.getLibelle());
        if (tache.getDateLimite() != null)
            tacheExistante.setDateLimite(tache.getDateLimite());
        if (tache.getNiveau() != null)
            tacheExistante.setNiveau(tache.getNiveau());
    }


    private static void verifierTache(TacheRequestDto tacheRequestDto) throws TacheException {
        if (tacheRequestDto == null)
            throw new TacheException("la tacheRequestDto est nulle");

        if (tacheRequestDto.libelle() == null || tacheRequestDto.libelle().isBlank())
            throw new TacheException("le libelle est absent");

        if (tacheRequestDto.niveau() == null)
            throw new TacheException("le niveau est absent");

        if (tacheRequestDto.dateLimite() == null)
            throw new TacheException("la date limite est absente");

        if (tacheRequestDto.termine() == null) {
            throw new TacheException("le 'termine' est absent");
        }
    }

}
