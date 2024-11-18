package pl.zespolowy.Business.Algorithm;

import lombok.Getter;
import lombok.Setter;
import pl.zespolowy.Language;
import pl.zespolowy.Word;
import pl.zespolowy.WordSet;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class WordSetsRegrouper {
    private final WordSetsTranslation wordSetsTranslation;
    Set<Map<Language, Word>> mapSet = new HashSet<>();

    public WordSetsRegrouper(WordSetsTranslation wordSetsTranslation) {
        this.wordSetsTranslation = wordSetsTranslation;
    }

    /**
     *
     */
    public void regroupMapsOfLanguageAndWordSetsToSetOfMapsOfLanguageAndWords() {
        Map<Language, WordSet> wordSetsInDifferentLanguages = wordSetsTranslation.getWordSetsInDifferentLanguages();
        int size = findSmallestWordSet(wordSetsInDifferentLanguages);
        for (int i = 0; i < size; i++) {
            int finalI = i;
            Map<Language, Word> languageWordMap = getLanguageWordMap(wordSetsInDifferentLanguages, finalI);
            mapSet.add(languageWordMap);
        }
    }

    private static int findSmallestWordSet(Map<Language, WordSet> wordSetsInDifferentLanguages) {
        int leastLong = 100;
        for (var values : wordSetsInDifferentLanguages.values()) {
            if (values.getWords().size()-1 < leastLong) {
                leastLong = values.getWords().size()-1;
            }
        }

        return leastLong;
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
