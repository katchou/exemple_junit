package formation.cap.cnaf.demojunit.exemple;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import formation.cap.cnaf.demojunit.exemple.util.BailleurResource;
import formation.cap.cnaf.demojunit.exemple.util.CampagneLoyerInDto;
import formation.cap.cnaf.demojunit.exemple.util.CampagneResource;
import formation.cap.cnaf.demojunit.exemple.util.DeclarationLoyerStat;
import formation.cap.cnaf.demojunit.exemple.util.DeclarationLoyerStatRepository;
import formation.cap.cnaf.demojunit.exemple.util.DeclarationResource;
import formation.cap.cnaf.demojunit.exemple.util.TraitementAutonomeStat;
import formation.cap.cnaf.demojunit.exemple.util.TraitementAutonomeStatRepository;


/**
 * Classe de base de consolidation des statistiques
 * 
 * @author jjung755
 *
 */
@Component("main")
public class AbstractConsolidationStatistiques {

    private final Logger log = LoggerFactory.getLogger(AbstractConsolidationStatistiques.class);

    @Autowired
    protected DeclarationResource declarationResource;

    @Autowired
    protected CampagneResource campagneResource;

    @Autowired
    protected BailleurResource bailleurResource;

    @Autowired
    protected DeclarationLoyerStatRepository declarationLoyerStatRepository;

    @Autowired
    private TraitementAutonomeStatRepository traitementAutonomeRepository;

    protected ZonedDateTime dateDebut;
    protected ZonedDateTime dateFin;
    protected LocalDate dateReference;
    protected LocalDate dateRefMonth;
    protected Integer anneeCampagne;
    protected Integer anneeEnCours;

    /**
     * Méthode d'initialisation pour la consolidation des statistiques autonomes.
     * La valeur de retour indique si la consolidation est nécessaire 
     * 
     * @param numOrg organisme pour lequel initialiser la consolidation
     * 
     * @return true si la consolidation est nécessaire
     */
    public boolean initAutonome(final String numOrg) {
        LocalDate dateAConsolider = null;

        Optional<TraitementAutonomeStat> derniereConsolidation = traitementAutonomeRepository.findFirstByNumOrgOrderByDateReferenceDesc(numOrg);
        if (!derniereConsolidation.isPresent()) {
            Optional<LocalDate> datePremiereDeclaration = declarationResource.getDatePremiereDeclaration(numOrg);
            if (datePremiereDeclaration.isPresent()) {
                dateAConsolider = datePremiereDeclaration.get();
            } else {
                log.info("Pas de consolidation des déclarations autonomes pour {} : pas de changements connus", numOrg);
                return false;
            }
        } else {
            if (derniereConsolidation.get().getDateReference().plusDays(1).equals(LocalDate.now())) {
                log.info("Pas de consolidation des déclarations autonomes pour {} : déjà effectuée hier", numOrg);
                return false;
            }
            dateAConsolider = derniereConsolidation.get().getDateReference().plusDays(1);
        }
        log.info("Date de départ de la consolidation : {}", dateAConsolider);
        this.init(numOrg, dateAConsolider);
        return true;
    }

    /**
     * Méthode d'initialisation pour la consolidation des statistiques campagne loyer.
     * La valeur de retour indique si la consolidation est nécessaire.
     * 
     * @param numOrg organisme pour lequel initialiser la consolidation
     * 
     * @return true si la consolidation est nécessaire
     */
    public boolean initCampagne(final String numOrg) {
        Optional<CampagneLoyerInDto> campagneDto = campagneResource.getDerniereCampagne(numOrg);

        if (!campagneDto.isPresent()) {
            log.info("Pas de consolidation possible des loyers autonomes pour {} : pas de campagne connue", numOrg);
            return false;
        }

        Optional<DeclarationLoyerStat> derniereConsolidation = declarationLoyerStatRepository.findFirstByNumOrgOrderByDateReferenceDesc(numOrg);
        LocalDate dateAConsolider = null;
        if (!derniereConsolidation.isPresent()) {
            Optional<LocalDate> datePremiereDeclaration = declarationResource.getDatePremiereDeclaration(numOrg);
            if (datePremiereDeclaration.isPresent()) {
                dateAConsolider = datePremiereDeclaration.get();
            } else {
                log.info("Pas de consolidation possible des loyers autonomes pour {} : ni traitement existant ni déclarations connues", numOrg);
                return false;
            }
        } else {
            if (campagneDto.get().getDateFin().toLocalDate().equals(derniereConsolidation.get().getDateReference())) {
                log.info("Pas de consolidation possible des loyers autonomes pour {} : toutes les données de campagne ont été consolidées", numOrg);
                return false;
            } else if (derniereConsolidation.get().getDateReference().plusDays(1).equals(LocalDate.now())) {
                log.info("Pas de consolidation des déclarations autonomes pour {} : déjà effectuée hier", numOrg);
                return false;
            }
            dateAConsolider = derniereConsolidation.get().getDateReference().plusDays(1);
        }

        log.info("Date de départ de la consolidation : {}", dateAConsolider);
        this.init(numOrg, dateAConsolider);
        return true;
    }

    private void init(final String numOrg, final LocalDate statDate) {
        log.debug("initialisation des variables pour les calcules des stats");
        dateReference = statDate;
        dateRefMonth = statDate;
        dateDebut = ZonedDateTime.of(dateReference, LocalTime.MIN, ZoneId.systemDefault());
        dateFin = ZonedDateTime.of(dateReference, LocalTime.MAX, ZoneId.systemDefault());
        anneeEnCours = dateReference.getYear();
    }

    public void init(final String numOrg) {
        log.debug("initialisation des variables pour les calcules des stats");
        dateReference = LocalDate.now();
        dateDebut = ZonedDateTime.of(dateReference, LocalTime.MIN, ZoneId.systemDefault());
        dateFin = ZonedDateTime.of(dateReference, LocalTime.MAX, ZoneId.systemDefault());
        anneeEnCours = dateReference.getYear();
    }

    protected BigDecimal calculerTaux(final Integer nb, final Integer nbTotal) {
        return nb != null && nbTotal != null && nbTotal != 0 ? new BigDecimal((double) nb / nbTotal * 100) : new BigDecimal(0);
    }

    public ZonedDateTime getDateDebut() {
        return dateDebut;
    }

    public ZonedDateTime getDateFin() {
        return dateFin;
    }

    public LocalDate getDateReference() {
        return dateReference;
    }

    public LocalDate getDateRefMonth() {
        return dateRefMonth;
    }

    public Integer getAnneeCampagne() {
        return anneeCampagne;
    }

    public Integer getAnneeEnCours() {
        return anneeEnCours;
    }

}
