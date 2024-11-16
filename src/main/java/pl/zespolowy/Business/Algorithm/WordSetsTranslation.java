package pl.zespolowy.Business.Algorithm;

import lombok.Getter;
import lombok.Setter;
import pl.zespolowy.Language;
import pl.zespolowy.Translator;
import pl.zespolowy.Word;
import pl.zespolowy.WordSet;

import java.util.*;
import java.util.stream.Collectors;


@Getter
@Setter
public class WordSetsTranslation {
    private Translator translator = new Translator();
    Map<Language, WordSet> wordSetsInDifferentLanguages = new HashMap<>();

    /**
     * @param language
     * @param wordSet  return
     * @return
     */
    public void translateWordSetToOtherLanguage(Language language, WordSet wordSet) {
        String joinedString = wordsListToString(wordSet);
        String translatedString = translator.translate(joinedString, "en", language.getCode()).getText();
        wordSetsInDifferentLanguages.put(language, new WordSet(wordSet.getTitle(), stringToListOfWords(translatedString)));
    }

    /**
     *
     * @param wordSet
     * @return String
     */
    private static String wordsListToString(WordSet wordSet) {
        return wordSet.getWords().stream()
                .map(Word::getText)
                .collect(Collectors.joining(", "));
    }

    /**
     *
     * @param translatedString
     * @return List<Word>
     */
    private static List<Word> stringToListOfWords(String translatedString) {
        return Arrays.stream(translatedString.split(", "))
                .map(Word::new)
                .toList();
    }
}
