package pl.zespolowy.Business.Algorithm;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class WordsProximityNormalizer {
    private final LanguageTranslationAndSimilarityCalculator languageTranslationAndSimilarityCalculator;
    private final Set<LanguageProximityResult> resultsSet;
    private Map<LanguageProximityResult, Double> countedProximityBetweenWords = new HashMap<>();

    public WordsProximityNormalizer(LanguageTranslationAndSimilarityCalculator languageTranslationAndSimilarityCalculator) {

        this.languageTranslationAndSimilarityCalculator = languageTranslationAndSimilarityCalculator;
        this.resultsSet = (Set<LanguageProximityResult>) languageTranslationAndSimilarityCalculator.getProximityBetweenTwoLanguagesMap().values();
        countedProximityBetweenWords = normalizationBetweenWords();
    }

    /**
     * na razie skupiamy sie na jezykach -> tematy nie zmieniaja nic poza wieksza iloscia slow
     */
    public Map<LanguageProximityResult, Double> normalizationBetweenWords() {
        return countedProximityBetweenWords = resultsSet.stream()
                .collect(Collectors.toMap(
                        lpr -> lpr,
                        lpr -> {
                            Double val1 = Double.valueOf(lpr.getCountedProximity().get());
                            Double val2 = Double.valueOf(lpr.getNumberOfWordsToNormalization().get());
                            return val1 / val2;
                        }
                ));
    }


}
