package formation.cap.cnaf.demojunit;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;

//@ComponentScan(basePackages = { "fr.cnaf.statistiques.batch", "fr.cnaf.middle.statistiques" })
//@EntityScan(basePackages = { "fr.cnaf.middle.statistiques.domain" })
@EnableAutoConfiguration(exclude = { JmxAutoConfiguration.class })
@SpringBootApplication
public class StatistiquesBailleursBatchAppTest {

}
