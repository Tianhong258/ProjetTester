package com.accenture.service;

import com.accenture.exception.TacheException;
import com.accenture.model.Priorite;
import com.accenture.repository.TacheDao;
import com.accenture.repository.entity.Tache;
import com.accenture.service.dto.TacheRequestDto;
import com.accenture.service.dto.TacheResponseDto;
import com.accenture.service.mapper.TacheMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)// pour dire à JUnit5 qu'on va utiliser les @Mock
class TacheServiceImplTest {


    @Mock
    TacheDao daoMock;
    @Mock
    TacheMapper mapperMock;
    @InjectMocks
    TacheServiceImpl service;

    @BeforeEach
    void init() {
        //daoMock= Mockito.mock(TacheDao.class);
        //mapperMock= Mockito.mock(TacheMapper.class);
        //service = new TacheServiceImpl(daoMock,mapperMock);
    }


    @DisplayName("""
            Test de la méthode trouver(int id) qui doit renvoyer une exception 
            lors que la tâche n'existe pas en base
            """)
    @Test
    void testTrouverExistePas() {
        //simulation que la tâche n'existe pas en base
        Mockito.when(daoMock.findById(50)).thenReturn(Optional.empty());
        //appeler la méthode qu'on test pour vérifier que ça marche
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.trouver(50));
        assertEquals("id non présent", ex.getMessage());
    }

    @DisplayName("""
            Test de la méthode trouver(int id) qui doit renvoyer un TacheResponseDto 
            lors que la tâche existe en base
            """)
    @Test
    void testTrouverExiste() {
        //simulation que la tâche existe en base
        Tache t = creerTacheCourse();
        Optional<Tache> optTache = Optional.of(t);
        Mockito.when(daoMock.findById(1)).thenReturn(optTache);
        TacheResponseDto dto = creerTacheResponseDtoCourse();
        Mockito.when(mapperMock.toTacheResponseDto(t)).thenReturn(dto);
        assertSame(dto, service.trouver(1));//assertSame : same objet : même référence
    }

    @DisplayName("""
            Test de la méthode trouverToutes() qui doit renvoyer une liste TacheResponseDto 
            correspondant aux tâches existant en base
            """)
    @Test
    void testTrouverToutes() {
        Tache tacheCourse = creerTacheCourse();
        Tache tacheCadeau = creerTacheCadeau();
        List<Tache> taches = List.of(tacheCourse, tacheCadeau);
        TacheResponseDto tacheResponseDtoCourse = creerTacheResponseDtoCourse();
        TacheResponseDto tacheResponseDtoCadeau = creerTacheResponseDtoCadeau();
        List<TacheResponseDto> tachesDto = List.of(tacheResponseDtoCourse, tacheResponseDtoCadeau);
        Mockito.when(daoMock.findAll()).thenReturn(taches);
        Mockito.when(mapperMock.toTacheResponseDto(tacheCadeau)).thenReturn(tacheResponseDtoCadeau);
        Mockito.when(mapperMock.toTacheResponseDto(tacheCourse)).thenReturn(tacheResponseDtoCourse);
        assertEquals(tachesDto, service.trouverToutes());
    }

    @DisplayName("Si ajouter(null) exception levée")
    @Test
    void testAjouter() {
        assertThrows(TacheException.class, () -> service.ajouter(null));
    }

    @DisplayName("Si ajouter(TacheRequestDto avec libelle null) exception levée")
    @Test
    void testAjouterAvecLibelleNull() {
        TacheRequestDto dto = new TacheRequestDto(null, LocalDate.now(), Priorite.BAS, true);
        assertThrows(TacheException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter(TacheRequestDto avec libelle blank) exception levée")
    @Test
    void testAjouterAvecLibelleBlank() {
        TacheRequestDto dto = new TacheRequestDto("  \n\t ", LocalDate.now(), Priorite.BAS, true);
        assertThrows(TacheException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter(TacheRequestDto avec dateLimite null) exception levée")
    @Test
    void testAjouterAvecDateLimiteNull() {
        TacheRequestDto dto = new TacheRequestDto("Cool", null, Priorite.BAS, true);
        assertThrows(TacheException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter(TacheRequestDto avec niveau null) exception levée")
    @Test
    void testAjouterAvecNiveauNull() {
        TacheRequestDto dto = new TacheRequestDto("Cool", LocalDate.now(), null, true);
        assertThrows(TacheException.class, () -> service.ajouter(dto));
    }

    @DisplayName("Si ajouter(TacheRequestDto avec termine null) exception levée")
    @Test
    void testAjouterAvecTermineNull() {
        TacheRequestDto dto = new TacheRequestDto("Cool", LocalDate.now(), Priorite.BAS, null);
        assertThrows(TacheException.class, () -> service.ajouter(dto));
    }

    @DisplayName("""
            Si ajouter(TacheRequestDto ok), save() est appelé, et TacheResponseDto est renvoyé
            """)
    @Test
    void testAjouterOk() {
        Tache tacheAvantEnreg = creerTacheCourse();
        tacheAvantEnreg.setId(0);
        TacheRequestDto dto = new TacheRequestDto("faire la course", LocalDate.of(2024, 12, 24), Priorite.BAS, false);
        Tache tacheApresEnreg = creerTacheCourse();
        TacheResponseDto tacheResponseDto = creerTacheResponseDtoCourse();
        Mockito.when(mapperMock.toTache(dto)).thenReturn(tacheAvantEnreg);
        Mockito.when(daoMock.save(tacheAvantEnreg)).thenReturn(tacheApresEnreg);
        Mockito.when(mapperMock.toTacheResponseDto(tacheApresEnreg)).thenReturn(tacheResponseDto);
        assertSame(tacheResponseDto, service.ajouter(dto));
        Mockito.verify(daoMock, Mockito.times(1)).save(tacheAvantEnreg);
    }




    private static Tache creerTacheCourse() {
        Tache t = new Tache();
        t.setId(1);
        t.setDateLimite(LocalDate.of(2024, 12, 24));
        t.setLibelle("faire la course");
        t.setNiveau(Priorite.BAS);
        t.setTermine(false);
        return t;
    }

    private static Tache creerTacheCadeau() {
        Tache t = new Tache();
        t.setId(2);
        t.setDateLimite(LocalDate.of(2024, 12, 20));
        t.setLibelle("acheter un cadeau pour Guifei");
        t.setNiveau(Priorite.HAUT);
        t.setTermine(false);
        return t;
    }

    private static TacheResponseDto creerTacheResponseDtoCourse() {
        return new TacheResponseDto(
                1,
                "faire la course",
                LocalDate.of(2024, 12, 24),
                Priorite.BAS,
                false
        );
    }

    private static TacheResponseDto creerTacheResponseDtoCadeau() {
        return new TacheResponseDto(
                2,
                "acheter un cadeau pour Guifei",
                LocalDate.of(2024, 12, 20),
                Priorite.HAUT,
                false
        );
    }


}