/*
 *  Drools Online Course Sample Code and Study Materials (c) by Juhan Aasaru.
 *
 *  Drools Online Course Sample Code and Study Materials is licensed under a
 *  Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 *
 *  You should have received a copy of the license along with this
 *  work. If not, see <http://creativecommons.org/licenses/by-nc-nd/4.0/>.
 */

package io.github.aasaru.drools;

import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Log4j2
public class Common {

  public static boolean disposeSession = true;

  public static int promptForStep(int section, String[] args, int minStep, int maxStep) {
    String stepStr = "";

    while (true) {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      try {
        if (args != null && args.length > 0) {
          stepStr = args[0];
          return Integer.parseInt(stepStr);
        }
        else {
          System.out.print(String.format("Section %d. Enter step (%d...%d): ", section, minStep, maxStep));
          stepStr = br.readLine();
        }

        int step = Integer.parseInt(stepStr);

        if (step < minStep || step > maxStep) {
          log.info("Step number out of range. Insert a number between " + minStep + " and " + maxStep);
        }
        else {
          return step;
        }
      }
      catch (NumberFormatException e) {
        log.info("Invalid number: " + stepStr);
      }
      catch (IOException e) {
        log.info("Invalid step input: " + stepStr);
      }
    }

  }


  public static boolean promptForYesNoQuestion(String question) {
    String enteredStr = "";

    while (true) {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      try {
        System.out.print(String.format("%s Enter 'yes' or 'no': ", question));
        enteredStr = br.readLine().trim();

        if ("yes".equalsIgnoreCase(enteredStr) || "y".equalsIgnoreCase(enteredStr)) {
          return true;
        }

        if ("no".equalsIgnoreCase(enteredStr) || "n".equalsIgnoreCase(enteredStr)) {
          return false;
        }

        log.info("Enter either 'yes' or 'no'");
      }
      catch (IOException e) {
        log.info("Invalid input: " + enteredStr);
      }
    }

  }

}
