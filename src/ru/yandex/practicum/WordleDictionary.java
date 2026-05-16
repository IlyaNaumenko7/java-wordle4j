package ru.yandex.practicum;

import java.util.*;

public class WordleDictionary {
    private final List<String> words;

    public WordleDictionary(List<String> words) {
        this.words = new ArrayList<>(words);
    }

    public List<String> getWords() {
        return Collections.unmodifiableList(words);
    }

    public boolean contains(String word) {
        return words.contains(word);
    }

    public String getRandomWord() {
        return words.get(new Random().nextInt(words.size()));
    }

    public static String normalize(String input) {
        if (input == null) return "";
        return input.toLowerCase().replace('ё', 'е').trim();
    }

    /**
     * Фильтрует словарь по ограничениям, полученным из подсказок.
     * Сложность: O(N * L), где N — размер словаря, L=5 — длина слова.
     */
    public List<String> filter(Map<Integer, Character> exactMatches,
                               Set<Character> presentChars,
                               Set<Character> absentChars) {
        List<String> candidates = new ArrayList<>();
        for (String word : words) {
            if (matchesConstraints(word, exactMatches, presentChars, absentChars)) {
                candidates.add(word);
            }
        }
        return candidates;
    }

    private boolean matchesConstraints(String word, Map<Integer, Character> exact,
                                       Set<Character> present, Set<Character> absent) {
        for (Map.Entry<Integer, Character> entry : exact.entrySet()) {
            if (word.charAt(entry.getKey()) != entry.getValue()) return false;
        }
        for (char c : present) {
            if (!word.contains(String.valueOf(c))) return false;
        }
        for (char c : absent) {
            if (word.contains(String.valueOf(c))) return false;
        }
        return true;
    }
}