/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.util;

import java.util.HashMap;
import java.util.Map;

public class PasswordCipher {

    private final static String allLetters = "abcdefghijklmnopqrstuvwxyzABCDEFGHI" + "JKLMNOPQRSTUVWXYZ";
    private final static int shift = 5;

    public static String encode(String stringToEncode){
        Map<Character, Character> dict = new HashMap<>();
        for (int i = 0; i < allLetters.length(); i++) {
            dict.put(allLetters.charAt(i),
                    allLetters.charAt((i + shift) % allLetters.length()));
        }

        StringBuilder cipherText = new StringBuilder();

        // loop to generate ciphertext
        for (char c : stringToEncode.toCharArray()) {
            if (allLetters.indexOf(c) != -1) {
                cipherText.append(dict.get(c));
            } else {
                cipherText.append(c);
            }
        }

        return cipherText.toString();
    }

    public static String decode(String stringToDecode) {
        Map<Character, Character> dict = new HashMap<>();
        for (int i = 0; i < allLetters.length(); i++) {
            dict.put(allLetters.charAt(i),
                    allLetters.charAt(((i - shift) + allLetters.length()) % allLetters.length()));
        }

        StringBuilder decryptedText = new StringBuilder();

        // loop to recover plain text
        for (char c : stringToDecode.toCharArray()) {
            if (allLetters.indexOf(c) != -1) {
                decryptedText.append(dict.get(c));
            } else {
                decryptedText.append(c);
            }
        }

        return decryptedText.toString();
    }

}
