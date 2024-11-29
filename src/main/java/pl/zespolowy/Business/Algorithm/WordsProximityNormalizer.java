package pl.zespolowy.Business.Algorithm;


import lombok.Getter;
import pl.zespolowy.Language;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class WordsProximityNormalizer {
    private final LanguageSimilarityCalculator languageSimilarityCalculator;
    private final Map<String, Map<Map<LanguageProximityResult, Double>, Double>> countedProximityBetweenWords = new HashMap<>();
    private final Map<String, Map<Language, Map<Language, Double>>> mapPreparedForJson = new HashMap<>();
    private final WordSetsTranslation wordSetsTranslation;

    public WordsProximityNormalizer(LanguageSimilarityCalculator languageSimilarityCalculator, WordSetsTranslation wordSetsTranslation) {
        this.languageSimilarityCalculator = languageSimilarityCalculator;
        this.wordSetsTranslation = wordSetsTranslation;
        unpackMapOfTopics();
        getLPRResultMapByTopic();
    }

    public void getLPRResultMapByTopic() {
        List<Language> languageList = wordSetsTranslation.getLanguageList();

        for (var topic : countedProximityBetweenWords.entrySet()) {
            String key = topic.getKey();
            Map<Map<LanguageProximityResult, Double>, Double> value = topic.getValue();

            Map<LanguageProximityResult, Double> mapsOfLPR = value.keySet().iterator().next();
            Map<Language, Map<Language, Double>> languageResultMap = languageList.stream().collect(Collectors.toMap(language -> language, language -> new HashMap<>()));

            for (var entry : mapsOfLPR.entrySet()) {
                LanguageProximityResult key1 = entry.getKey();
                Double newVal = entry.getValue();


                Map<Language, Double> language1Map = languageResultMap.computeIfAbsent(key1.getLanguage2(), k -> new HashMap<>());
                language1Map.merge(key1.getLanguage1(), newVal, Double::sum);

                Map<Language, Double> language2Map = languageResultMap.computeIfAbsent(key1.getLanguage1(), k -> new HashMap<>());
                language2Map.merge(key1.getLanguage2(), newVal, Double::sum);
            }
            mapPreparedForJson.put(key, languageResultMap);
        }
    }

    public void unpackMapOfTopics() {
        var mapOfTopics = languageSimilarityCalculator.getProximityBetweenTwoLanguagesMapByTopic();
        for (var map : mapOfTopics.entrySet()) {
            var topicKey = map.getKey();
            var innerMap = map.getValue();
            var mapOfMaps = normalizationBetweenWords(innerMap);

            countedProximityBetweenWords.put(topicKey, mapOfMaps);
        }
    }
    public Map<Map<LanguageProximityResult, Double>, Double> normalizationBetweenWords(Map<String, LanguageProximityResult> resultMap) {
        var innerMap = resultMap.values().stream()
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
