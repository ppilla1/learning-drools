/*
 *  Drools Online Course Sample Code and Study Materials (c) by Juhan Aasaru.
 *
 *  Drools Online Course Sample Code and Study Materials is licensed under a
 *  Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 *
 *  You should have received a copy of the license along with this
 *  work. If not, see <http://creativecommons.org/licenses/by-nc-nd/4.0/>.
 */

package io.github.aasaru.drools.section08;

import io.github.aasaru.drools.Common;
import io.github.aasaru.drools.domain.*;
import io.github.aasaru.drools.repository.ApplicationRepository;
import lombok.extern.log4j.Log4j2;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieSession;

import java.util.Collection;
import java.util.List;

@Log4j2
public class FamilyVisaApplicationValidation {
  public static void main(final String[] args) {
    execute(Common.promptForStep(8, args, 1, 5));
  }

  static void execute(int step) {
    log.info("Running step " + step);
    KieSession ksession = KieServices.Factory.get().getKieClasspathContainer().newKieSession("FamilyVisaApplicationStep" + step);

    List<Passport> passports = ApplicationRepository.getPassports();
    passports.forEach(ksession::insert);

    if (step == 3) {
      if (Common.promptForYesNoQuestion("Do you want to make everyone 3 years younger?")) {
        log.info("Making everyone 3 years younger");
        passports.forEach(passport -> passport.setAge(passport.getAge()-3));
        passports.forEach(passport -> log.info(passport + " is now " + passport.getAge()));
      }
    }

    List<FamilyVisaApplication> familyVisaApplications = ApplicationRepository.getFamilyVisaApplications();
    familyVisaApplications.forEach(ksession::insert);

    log.info("==== DROOLS SESSION START ==== ");
    ksession.fireAllRules();
    if (Common.disposeSession) {
      ksession.dispose();
    }
    log.info("==== DROOLS SESSION END ==== ");

    log.info("==== INVALID FAMILY VISA APPLICATIONS FROM DROOLS SESSION === ");
    Collection<?> invalidApplications = ksession.getObjects(o -> o.getClass() == InvalidFamilyVisaApplication.class);
    invalidApplications.forEach(System.out::println);

    Collection<?> visas = ksession.getObjects(o -> o.getClass() == Visa.class);
    log.info("== Visas from session == ");
    visas.forEach(System.out::println);

    Collection<?> groupLeaders = ksession.getObjects(o -> o.getClass() == GroupLeader.class);
    log.info("== Group leaders from session == ");
    groupLeaders.forEach(System.out::println);

  }


}
