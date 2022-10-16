/*
 *  Drools Online Course Sample Code and Study Materials (c) by Juhan Aasaru.
 *
 *  Drools Online Course Sample Code and Study Materials is licensed under a
 *  Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 *
 *  You should have received a copy of the license along with this
 *  work. If not, see <http://creativecommons.org/licenses/by-nc-nd/4.0/>.
 */

package io.github.aasaru.drools.section03;

import io.github.aasaru.drools.Common;
import io.github.aasaru.drools.domain.Passport;
import io.github.aasaru.drools.repository.ApplicationRepository;
import lombok.extern.log4j.Log4j2;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

import java.util.List;


@Log4j2
public class StatelessPassportValidation {
  public static void main(final String[] args) {
    execute(Common.promptForStep(3, args, 1, 6));
  }

  static void execute(int step) {
    log.info("Running step {}", step);

    List<Passport> passports = ApplicationRepository.getPassports();

    KieContainer kieContainer = KieServices.Factory.get().getKieClasspathContainer();
    StatelessKieSession kieSession = kieContainer.newStatelessKieSession("StatelessPassportValidationStep" + step);
    log.info("==== DROOLS SESSION START ==== ");
    kieSession.execute(passports);
    log.info("==== DROOLS SESSION END ==== ");

    if (step >= 4) {
      log.info("==== PASSPORTS AFTER DROOLS SESSION ==== ");

      passports.forEach(passport -> log.info(passport + " validation " + passport.getValidation()));

    }

  }

}
