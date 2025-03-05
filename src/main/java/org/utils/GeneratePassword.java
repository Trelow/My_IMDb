package org.utils;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.passay.CharacterData;

public class GeneratePassword {

    public static String generateSecurePassword() {
        CharacterRule LCR = new CharacterRule(EnglishCharacterData.LowerCase);
        LCR.setNumberOfCharacters(2);

        CharacterRule UCR = new CharacterRule(EnglishCharacterData.UpperCase);
        UCR.setNumberOfCharacters(2);

        CharacterRule DR = new CharacterRule(EnglishCharacterData.Digit);
        DR.setNumberOfCharacters(2);

        CharacterRule SR = new CharacterRule(new CharacterData() {
            public String getErrorCode() {
                return null;
            }

            public String getCharacters() {
                return "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
            }
        });

        SR.setNumberOfCharacters(2);

        PasswordGenerator passGen = new PasswordGenerator();

        String password = passGen.generatePassword(12, SR, LCR, UCR, DR);
        return password;
    }
}