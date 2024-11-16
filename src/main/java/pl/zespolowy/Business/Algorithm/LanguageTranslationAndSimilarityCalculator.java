package pl.zespolowy.Business.Algorithm;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.text.similarity.LevenshteinDistance;
import pl.zespolowy.Language;
import pl.zespolowy.LanguageSet;
import pl.zespolowy.Word;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public final class LanguageTranslationAndSimilarityCalculator {
    private final String rootPath = System.getProperty("user.dir");
    private final String languagesPath = rootPath + "/languages.json";
    private LanguageSet languageSet;
    private List<Language> languageList = new ArrayList<>();
    private Map<String, LanguageProximityResult> proximityBetweenTwoLanguagesMap = new HashMap<>();
    private LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
    private WordSetsRegrouper wordSetsRegrouper;

    public LanguageTranslationAndSimilarityCalculator(WordSetsRegrouper wordSetsRegrouper) {
        this.wordSetsRegrouper = wordSetsRegrouper;
    }


    /**
     * obliczenie podobienstwa algorytmem levensteina pomiedzy slowem w roznych jezykach
     *
     * @param languages
     */
    public void countingProximityForWordInDifferentLanguagesAndPuttingResultToLanguageProximityResult(List<Language> languages) {
        var setOfMapsHavingWordsInDifferentLanguages = wordSetsRegrouper.getMapSet();
        var proximityBetweenTwoLanguagesMap = makeSetOfProximityBetweenTwoLanguages(languages);

        for (var set : setOfMapsHavingWordsInDifferentLanguages) {
            loopThroughMaps(set, proximityBetweenTwoLanguagesMap);
        }
    }

    /**
     *
     * @param set
     * @param proximityBetweenTwoLanguagesMap
     */
    private void loopThroughMaps(Map<Language, Word> set, Map<String, LanguageProximityResult> proximityBetweenTwoLanguagesMap) {
        for (Map.Entry<Language, Word> outerEntry : set.entrySet()) {
            Language outerKey = outerEntry.getKey();
            Word outerValue = outerEntry.getValue();

            var innerIterator = set.entrySet().iterator();
            // pomija elementy do outerEntry
            while (innerIterator.hasNext() && !innerIterator.next().equals(outerEntry)) {}

            while (innerIterator.hasNext()) {
                var innerEntry = innerIterator.next();
                Language innerKey = innerEntry.getKey();
                Word innerValue = innerEntry.getValue();
                // obliczenie podobienstwa
                Integer countedDistance = levenshteinDistance.apply(outerValue.getText(), innerValue.getText());
                putResultToLanguageProximityResult(outerKey, innerKey, proximityBetweenTwoLanguagesMap, countedDistance);
            }
        }
    }

    /**
     *
     * @param outerKey
     * @param innerKey
     * @param proximityBetweenTwoLanguagesMap
     * @param countedDistance
     */
    private static void putResultToLanguageProximityResult(Language outerKey, Language innerKey, Map<String, LanguageProximityResult> proximityBetweenTwoLanguagesMap, Integer countedDistance) {
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


    /**
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

