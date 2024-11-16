package pl.zespolowy.Business.Algorithm;

import lombok.Getter;
import lombok.Setter;
import pl.zespolowy.Language;
import pl.zespolowy.Word;
import pl.zespolowy.WordSet;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class WordSetsRegrouper {
    private final WordSetsTranslation wordSetsTranslation;
    WordSetsReader wsr;
    Set<Map<Language, Word>> mapSet = new HashSet<>();

    public WordSetsRegrouper(WordSetsTranslation wordSetsTranslation) throws IOException {
        this.wordSetsTranslation = wordSetsTranslation;
        this.wsr = new WordSetsReader("./src/main/resources/wordSets/", "fruits.json");
        wordSetsTranslation.translateWordSetToOtherLanguage(new Language("English", "en"),   wsr.getWordSet().get());
        wordSetsTranslation.translateWordSetToOtherLanguage(new Language("Polish", "pl"),   wsr.getWordSet().get());
    }

    /**
     *
     */
    public void regroupMapsOfLanguageAndWordSetsToSetOfMapsOfLanguageAndWords() {
        Map<Language, WordSet> wordSetsInDifferentLanguages = wordSetsTranslation.getWordSetsInDifferentLanguages();
        int size = wordSetsInDifferentLanguages.values().stream().findFirst().get().getWords().size()-1;
        for (int i = 0; i < size; i++) {
            int finalI = i;
            Map<Language, Word> languageWordMap = getLanguageWordMap(wordSetsInDifferentLanguages, finalI);
            mapSet.add(languageWordMap);
        }
    }

    /**
     *
     * @param wordSetsInDifferentLanguages
     * @param finalI
     * @return
     */
    private static Map<Language, Word> getLanguageWordMap(Map<Language, WordSet> wordSetsInDifferentLanguages, int finalI) {
        Map<Language, Word> languageWordMap = wordSetsInDifferentLanguages.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getWords().get(finalI)
                ));
        return languageWordMap;
    }
}
