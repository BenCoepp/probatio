package de.bencoepp.utils.validator;

public class StringValidator {

    public static boolean validateTitle(String title){
        return !title.contains(" ");
    }
}
