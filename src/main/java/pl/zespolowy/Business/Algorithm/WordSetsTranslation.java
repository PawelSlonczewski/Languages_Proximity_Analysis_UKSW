package pl.zespolowy.Business.Algorithm;

import lombok.Getter;
import lombok.Setter;
import pl.zespolowy.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


@Getter
@Setter
public class WordSetsTranslation {
    private Translator translator = new Translator();
    private Map<String, Map<Language, WordSet>> setMapOfKeyStringAndValueMapLanguageWordSet = new HashMap<>();
    private LanguageSet languageSet;
    private List<Language> languageList = new ArrayList<>();
    private final String rootPath = System.getProperty("user.dir");
    private final String languagesPath = rootPath + "/languages.json";
    WordSetsReader wsr;

    public WordSetsTranslation() throws IOException {
        this.wsr = new WordSetsReader("./src/main/resources/wordSets/");
        setMapOfKeyStringAndValueMapLanguageWordSet();
    }

    /**
     *
      */
    public void setMapOfKeyStringAndValueMapLanguageWordSet() {
        setMapOfKeyStringAndValueMapLanguageWordSet = wsr.getWordSets().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        v -> setLanguagesToUse(v.getValue())
        ));
    }

    /**
     *
     */
    public Map<Language, WordSet> setLanguagesToUse(WordSet wordSet) {
        Map<Language, WordSet> wordSetsInDifferentLanguages = new HashMap<>();
        getLanguagesFromFile();
        languageList.forEach(a -> translateWordSetToOtherLanguage(wordSetsInDifferentLanguages, new Language(a.getName(), a.getCode()), wordSet));
        translator.refreshDriver();
        return wordSetsInDifferentLanguages;
    }

    /**
     * @param language
     * @param wordSet
     * @return
     */
    public void translateWordSetToOtherLanguage(Map<Language, WordSet> wordSetsInDifferentLanguages, Language language, WordSet wordSet) {
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
                .distinct()
                .map(Word::new)
                .toList();
    }

    public void getLanguagesFromFile() {
        initLanguages("set1", languagesPath);
        languageList = languageSet.getLanguages();
    }

    public void initLanguages(String title, String path) {
        try {
            String content = Files.readString(Paths.get(path));
            languageSet = new LanguageSet(title, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
