package pl.zespolowy.Business.Algorithm;


import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class WordsProximityNormalizer {
    private final LanguageSimilarityCalculator languageSimilarityCalculator;
    private final Map<String, Map<Map<LanguageProximityResult, Double>, Double>> countedProximityBetweenWords = new HashMap<>();

    public WordsProximityNormalizer(LanguageSimilarityCalculator languageSimilarityCalculator) {
        this.languageSimilarityCalculator = languageSimilarityCalculator;
        unpackMapOfTopics();
    }

    public void unpackMapOfTopics() {
        var mapOfTopics = languageSimilarityCalculator.getProximityBetweenTwoLanguagesMapByTopic();
        for (var map : mapOfTopics.entrySet()) {
            String topicKey = map.getKey();
            Map<String, LanguageProximityResult> innerMap = map.getValue();
            Map<Map<LanguageProximityResult, Double>, Double> mapOfMaps = normalizationBetweenWords(innerMap);

            countedProximityBetweenWords.put(topicKey, mapOfMaps);
        }
    }
    public Map<Map<LanguageProximityResult, Double>, Double> normalizationBetweenWords(Map<String, LanguageProximityResult> resultMap) {
        Map<LanguageProximityResult, Double> innerMap = resultMap.values().stream()
                .collect(Collectors.toMap(
                        lpr -> lpr,
                        lpr -> {
                            Double val1 = Double.valueOf(lpr.getCountedProximity().get());
                            Double val2 = Double.valueOf(lpr.getNumberOfWordsToNormalization().get());
                            return val1 / val2;
                        }
                ));
        double sum = innerMap.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();
        double average = innerMap.isEmpty() ? 0.0 : sum / innerMap.size();

        Map<Map<LanguageProximityResult, Double>, Double> result = new HashMap<>();
        result.put(innerMap, average);

        return result;
    }
}
