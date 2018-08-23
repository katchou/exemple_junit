package formation.cap.cnaf.demojunit;

import static org.junit.Assert.*;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import formation.cap.cnaf.demojunit.exemple.AbstractConsolidationStatistiques;
import formation.cap.cnaf.demojunit.exemple.util.BailleurResource;
import formation.cap.cnaf.demojunit.exemple.util.CampagneLoyerInDto;
import formation.cap.cnaf.demojunit.exemple.util.CampagneResource;
import formation.cap.cnaf.demojunit.exemple.util.DeclarationLoyerStat;
import formation.cap.cnaf.demojunit.exemple.util.DeclarationLoyerStatRepository;
import formation.cap.cnaf.demojunit.exemple.util.DeclarationResource;
import formation.cap.cnaf.demojunit.exemple.util.TraitementAutonomeStat;
import formation.cap.cnaf.demojunit.exemple.util.TraitementAutonomeStatRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StatistiquesBailleursBatchAppTest.class)
@ActiveProfiles({ "test", "debug" })
public class AbstractConsolidationStatistiquesTest {

    /**
     * Les tests sont effectué avec le numéro d'organisme 203 parce que "jamais 2 sans 3" (merci Titouan)   
     */
    private static final String numOrg = "203";

    @Autowired
    @Qualifier("main")
    private AbstractConsolidationStatistiques consolidation;

    @MockBean
    private DeclarationResource declarationResource;

    @MockBean
    private CampagneResource campagneResource;

    @MockBean
    private BailleurResource bailleurResource;

    @MockBean
    private DeclarationLoyerStatRepository declarationLoyerStatRepository;

    @MockBean
    private TraitementAutonomeStatRepository traitementAutonomeRepository;

    @BeforeClass
    public static void beforeClass() {
//        System.setProperty("hazelcast.logging.type", "log4j");
//        System.setProperty("ParmPath", "./src/test/resources/ressources");
//        System.setProperty("DiamsBatch", "true");
//        String parameterPath = System.getProperty("ParmPath") + "/pba";
//
//        CnafGlobalParam cnafGlobalParam = CnafGlobalParam.getInstance();
//        cnafGlobalParam.setFullParmPath(parameterPath);
//        cnafGlobalParam.setLog4jName("Log4j.properties");
    }

}

