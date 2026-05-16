package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    public WordleDictionary load(String filename) throws DictionaryLoadException {
        List<String> validWords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(filename, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String normalized = WordleDictionary.normalize(line);
                if (normalized.length() == 5 && normalized.matches("[а-яё]+")) {
                    validWords.add(normalized);
                }
            }
        } catch (IOException e) {
            throw new DictionaryLoadException("Ошибка чтения файла словаря: " + filename, e);
        }

        if (validWords.isEmpty()) {
            throw new DictionaryLoadException("Словарь пуст или не содержит слов из 5 букв", null);
        }
        return new WordleDictionary(validWords);
    }
}