package org.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import org.exceptions.InvalidCommandException;

import java.text.ParseException;

public class InputManager {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static int ToPositiveNumber(String input) {
        if (input != null)
            if (isNumber(input))
                return Integer.parseInt(input);

        return -1;
    }

    public static int ReadAPositiveNumber() {
        String input = "";
        input = ReadLine();

        while (!isNumber(input) && ToPositiveNumber(input) < 0) {
            System.out.println("Input must be a number");
            input = ReadLine();
        }

        return ToPositiveNumber(input);
    }

    public static boolean isNumber(String input) {
        return input.matches("\\d+");
    }

    public static String ReadLine() {
        String input = "";
        do {
            try {
                input = reader.readLine();
                if (input.length() == 0)
                    throw new InvalidCommandException("Input cannot be empty");
            } catch (IOException e) {
                System.out.println("IOException occurred");
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        } while (input.length() == 0);
        return input;
    }

    public static void WaitForInput() {
        System.out.println(
                "\nPress Enter to return to the productions list");
        try {
            reader.readLine();
        } catch (IOException e) {
            System.out.println("IOException occurred");
        }
    }

    public static String ReadLineWithoutNullException() {
        String input = "";
        try {
            input = reader.readLine();
        } catch (IOException e) {
            System.out.println("IOException occurred");
        }
        return input;
    }

    public static boolean IsDate(String input) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(input);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}
