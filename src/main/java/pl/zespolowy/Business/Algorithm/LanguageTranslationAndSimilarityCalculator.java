package pl.zespolowy.Business.Algorithm;
import lombok.*;
import pl.zespolowy.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.text.similarity.LevenshteinDistance;

@Getter
@Setter
@RequiredArgsConstructor
public final class LanguageTranslationAndSimilarityCalculator {
    // zapozyczone
    private final String rootPath = System.getProperty("user.dir");
    private final String languagesPath = rootPath + "/languages.json";
    private LanguageSet languageSet;
    private Translator translator = new Translator();

    private List<String> wordsList = List.of("Apple", "Banana", "Orange", "Mango", "Pineapple");
    private List<Language> languageList = new ArrayList<>();
    private Map<String, List<String>> topicListOfWordsMap = Map.of("fruits", wordsList);
    LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
    Map<String, LanguageProximityResult> proximityBetweenTwoLanguagesMap = new HashMap<>();
    Map<Language, Word> languageWordMap = new HashMap<>();

    /**
     * obliczenie podobienstwa algorytmem levensteina pomiedzy slowem w roznych jezykach
     * @param languages
     */
    public void countingProximityForWordInDifferentLanguagesAndPuttingResultToLanguageProximityResult(List<Language> languages) {
        var languageWordMap = translateWordToDifferentLanguages(new Word("Apple"), languages);
        var proximityBetweenTwoLanguagesMap = makeSetOfProximityBetweenTwoLanguages(languages);

        for (Map.Entry<Language, Word> outerEntry : languageWordMap.entrySet()) {
            Language outerKey = outerEntry.getKey();
            Word outerValue = outerEntry.getValue();

            var innerIterator = languageWordMap.entrySet().iterator();
            // pomija elementy do outerEntry
            while (innerIterator.hasNext() && !innerIterator.next().equals(outerEntry)) {}

            while (innerIterator.hasNext()) {
                var innerEntry = innerIterator.next();
                Language innerKey = innerEntry.getKey();
                Word innerValue = innerEntry.getValue();
                // obliczenie podobienstwa
                Integer countedDistance = levenshteinDistance.apply(outerValue.getText(), innerValue.getText());
                String languagesAbbreviation = outerKey.getCode() + innerKey.getCode();
                String languagesAbbreviationReversed = innerKey.getCode() + outerKey.getCode();

                if (proximityBetweenTwoLanguagesMap.containsKey(languagesAbbreviation)) {
                    proximityBetweenTwoLanguagesMap.get(languagesAbbreviation).countedProximityAndNumberOfWordsToNormalizationIncrease(countedDistance, 1);
                } else if (proximityBetweenTwoLanguagesMap.containsKey(languagesAbbreviationReversed)) {
                    proximityBetweenTwoLanguagesMap.get(languagesAbbreviationReversed).countedProximityAndNumberOfWordsToNormalizationIncrease(countedDistance, 1);
                } else {
                    System.out.println("LanguageProximityError: Abbreviation not found, line: 68");
                }
            }
        }
    }


    /**
     *
     * @param languages
     * @return Set<ProximityBetweenTwoLanguages>
     */
    public Map<String, LanguageProximityResult> makeSetOfProximityBetweenTwoLanguages(List<Language> languages) {
        for (int i = 0; i < languages.size(); i++) {
            for (int j = i + 1; j < languages.size(); j++) {
                proximityBetweenTwoLanguagesMap.put(languages.get(i).getCode() + languages.get(j).getCode(), new LanguageProximityResult(languages.get(i), languages.get(j)));
            }
        }
        return proximityBetweenTwoLanguagesMap;
    }

    /**
     * Można z stream zrobić parrallelStream ale wtedy trzebaby pozostałe klasy zrobić thread-safe
     * @param word
     * @param languageList
     * @return Map<Language, Word>
     */
    public Map<Language, Word> translateWordToDifferentLanguages(Word word, List<Language> languageList) {
        return languageList.stream()
                .collect(Collectors.toMap(
                        language -> language,
                        language -> new Word(translator.translate(word.getText(), "en", language.getCode()).getText())
                ));
    }

    private String mergeListElementsToString() {
        return String.join("; ", wordsList.get(0));
    }

    public void getLanguages() {
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

