package form;

import javax.swing.*;

public interface UsernameValidator {

    String GMAIL = "@gmail.com";
    boolean checkEmptyUsername(String username);
    boolean checkEndsWithAtUsername(String username);
    boolean checkStartsWithAtUsername(String username);
    boolean checkUsername();

}
