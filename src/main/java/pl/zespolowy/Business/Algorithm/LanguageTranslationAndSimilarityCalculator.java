package pl.zespolowy.Business.Algorithm;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.text.similarity.LevenshteinDistance;
import pl.zespolowy.Language;
import pl.zespolowy.Word;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public final class LanguageTranslationAndSimilarityCalculator {
    private LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
    private WordSetsRegrouper wordSetsRegrouper;
    private Map<String, LanguageProximityResult> proximityBetweenTwoLanguagesMap = new HashMap<>();

    public LanguageTranslationAndSimilarityCalculator(WordSetsRegrouper wordSetsRegrouper) {
        this.wordSetsRegrouper = wordSetsRegrouper;
    }


    /**
     * obliczenie podobienstwa algorytmem levensteina pomiedzy slowem w roznych jezykach
     *
     */
    public void countingProximityForWordInDifferentLanguagesAndPuttingResultToLanguageProximityResult() {
        var setOfMapsHavingWordsInDifferentLanguages = wordSetsRegrouper.getMapSet();
        var proximityBetweenTwoLanguagesMap = makeSetOfProximityBetweenTwoLanguages(wordSetsRegrouper.getWordSetsTranslation().getLanguageList());

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
     *
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

}

