package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;

public class WordleGame {
    private final String answer;
    private int stepsLeft;
    private final WordleDictionary dictionary;
    private final PrintWriter log;

    private final Set<String> usedWords = new HashSet<>();
    private final Set<String> usedHints = new HashSet<>();

    private final Map<Integer, Character> exactMatches = new HashMap<>();
    private final Set<Character> presentChars = new HashSet<>();
    private final Set<Character> absentChars = new HashSet<>();

    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this.dictionary = dictionary;
        this.log = log;
        this.answer = dictionary.getRandomWord();
        this.stepsLeft = 6;
        log.println("Game initialized. Answer set. Steps: 6");
    }

    public String makeMove(String input) throws WordNotFoundException, InvalidWordException {
        if (stepsLeft <= 0) {
            throw new GameLogicException("Попытки закончились");
        }

        String guess = WordleDictionary.normalize(input);
        if (guess.length() != 5 || !guess.matches("[а-яё]+")) {
            throw new InvalidWordException("Введите слово из 5 русских букв");
        }
        if (!dictionary.contains(guess)) {
            throw new WordNotFoundException("Слово отсутствует в словаре");
        }
        if (usedWords.contains(guess)) {
            throw new InvalidWordException("Это слово уже было использовано");
        }

        usedWords.add(guess);
        stepsLeft--;

        String feedback = compareWords(guess, answer);
        updateConstraints(guess, feedback);

        StringBuilder statusLog = new StringBuilder()
                .append("Move: ").append(guess)
                .append(" | Feedback: ").append(feedback)
                .append(" | Steps left: ").append(stepsLeft);
        log.println(statusLog.toString());

        return feedback;
    }

    private String compareWords(String guess, String answer) {
        char[] result = new char[5];
        Arrays.fill(result, '-');
        Map<Character, Integer> answerCounts = new HashMap<>();

        for (char c : answer.toCharArray()) {
            answerCounts.put(c, answerCounts.getOrDefault(c, 0) + 1);
        }

        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                result[i] = '+';
                answerCounts.put(guess.charAt(i), answerCounts.get(guess.charAt(i)) - 1);
            }
        }

        for (int i = 0; i < 5; i++) {
            if (result[i] == '-') {
                char c = guess.charAt(i);
                if (answerCounts.getOrDefault(c, 0) > 0) {
                    result[i] = '^';
                    answerCounts.put(c, answerCounts.get(c) - 1);
                }
            }
        }
        return new String(result);
    }

    private void updateConstraints(String guess, String feedback) {
        for (int i = 0; i < 5; i++) {
            char c = guess.charAt(i);
            char f = feedback.charAt(i);
            if (f == '+') {
                exactMatches.put(i, c);
            } else if (f == '^') {
                presentChars.add(c);
            } else {
                absentChars.add(c);
            }
        }

        exactMatches.values().forEach(absentChars::remove);
        presentChars.forEach(absentChars::remove);
    }

    public String getHint() {
        List<String> candidates = dictionary.filter(exactMatches, presentChars, absentChars);
        candidates.removeAll(usedWords);
        candidates.removeAll(usedHints);

        if (candidates.isEmpty()) {
            return "Нет доступных подсказок";
        }

        String hint = candidates.get(new Random().nextInt(candidates.size()));
        usedHints.add(hint);
        log.println("Hint generated: " + hint);
        return hint;
    }

    public boolean isGameOver() {
        return stepsLeft == 0;
    }

    public int getStepsLeft() {
        return stepsLeft;
    }

    public String getAnswer() {
        return answer;
    }
}