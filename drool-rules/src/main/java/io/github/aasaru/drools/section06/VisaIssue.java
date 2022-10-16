/*
 *  Drools Online Course Sample Code and Study Materials (c) by Juhan Aasaru.
 *
 *  Drools Online Course Sample Code and Study Materials is licensed under a
 *  Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 *
 *  You should have received a copy of the license along with this
 *  work. If not, see <http://creativecommons.org/licenses/by-nc-nd/4.0/>.
 */

package io.github.aasaru.drools.section06;

import io.github.aasaru.drools.Common;
import io.github.aasaru.drools.domain.Passport;
import io.github.aasaru.drools.domain.Visa;
import io.github.aasaru.drools.domain.VisaApplication;
import io.github.aasaru.drools.repository.ApplicationRepository;
import lombok.extern.log4j.Log4j2;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.Agenda;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Log4j2
public class VisaIssue {
  public static void main(final String[] args) {
    execute(Common.promptForStep(6, args, 1, 6));
  }

  static void execute(int step) {
    log.info("Running step " + step);
    KieSession ksession = KieServices.Factory.get().getKieClasspathContainer().newKieSession("VisaIssueStep" + step);

    ksession.addEventListener(new AgendaGroupEventListener(System.out));

    List<Passport> passports = ApplicationRepository.getPassports();

    if (step == 5) {
      if (Common.promptForYesNoQuestion("Do you want to set all passports as expired?")) {
        log.info("Setting all passports as expired before Drools session starts");
        passports.forEach(passport -> passport.setExpiresOn(LocalDate.MIN));
      }
    }

    passports.forEach(ksession::insert);


    List<VisaApplication> visaApplications = ApplicationRepository.getVisaApplications();
    visaApplications.forEach(ksession::insert);


    if (step == 3) {
      Agenda agenda = ksession.getAgenda();
      agenda.getAgendaGroup("issue-visa").setFocus();
      agenda.getAgendaGroup("valid-application").setFocus();
      agenda.getAgendaGroup("invalid-application").setFocus();
      agenda.getAgendaGroup("valid-passport").setFocus();
      agenda.getAgendaGroup("invalid-passport").setFocus();
    }


    if (step == 4 || step == 5) {
      Agenda agenda = ksession.getAgenda();
      agenda.getAgendaGroup("issue-visa").setFocus();
      agenda.getAgendaGroup("validate-application").setFocus();
      agenda.getAgendaGroup("validate-passport").setFocus();
    }


    /** BONUS STEP: set focus to first agenda group only */
    if (step == 6) {
      Agenda agenda = ksession.getAgenda();
      agenda.getAgendaGroup("validate-passport").setFocus();
    }

    log.info("==== DROOLS SESSION START ==== ");
    ksession.fireAllRules();
    if (Common.disposeSession) {
      ksession.dispose();
    }
    log.info("==== DROOLS SESSION END ==== ");

    Collection<?> visaObjects = ksession.getObjects(o -> o.getClass() == Visa.class);
    log.info("== Visas from session == ");
    visaObjects.forEach(System.out::println);

  }

}
