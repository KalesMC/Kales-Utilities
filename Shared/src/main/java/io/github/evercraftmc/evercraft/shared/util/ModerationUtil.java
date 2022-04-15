package io.github.evercraftmc.evercraft.shared.util;

public class ModerationUtil {
    private static final String[] bannedRegexes = new String[] { "ass(hole)?(s)?", "bastard(s)?", "bitch(es)?", "blowjob(s)?", "cock(s)?", "cocksucker(s)?", "cunt(s)?", "cum(s)?", "cumsock(s)?", "dick(s)?", "fu(c)?k(er)?(s)?", "fuck(ing)?", "motherfuck(er)?(s)?", "gay", "nigg(a)?(s)?", "nig(g)?er(s)?", "penis(es)?", "pp(s)", "porn", "pornstar(s)?", "prostitute(s)?", "pussy", "pussies", "shit(s)?", "shit(er)?", "slut(s)?", "sex(y)?", "vag", "vagina(s)?", "whore(s)?" };
    private static final String[] bannedWords = new String[] { "fu", ".|." };

    public static boolean isInappropriateString(String string) {
        for (String word : string.replace("-", " ").replace("_", " ").split(" ")) {
            if (isInappropriateWord(word)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInappropriateWord(String word) {
        for (String bannedRegex : bannedRegexes) {
            if (word.toLowerCase().matches(bannedRegex)) {
                return true;
            }
        }

        for (String bannedWord : bannedWords) {
            if (word.equalsIgnoreCase(bannedWord)) {
                return true;
            }
        }

        return false;
    }
}