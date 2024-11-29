package pl.zespolowy.Business.Algorithm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

@Getter
public class ProximityResultJSONExporter {
    private final WordsProximityNormalizer wordsProximityNormalizer;
    private final ObjectMapper objectMapper  = new ObjectMapper();

    public ProximityResultJSONExporter(WordsProximityNormalizer wordsProximityNormalizer) {
        this.wordsProximityNormalizer = wordsProximityNormalizer;
    }

    public void createJson() throws JsonProcessingException {
        var mapPreparedForJson = wordsProximityNormalizer.getMapPreparedForJson();
        String mapAsString = objectMapper.writeValueAsString(mapPreparedForJson);
        System.out.println(mapAsString);
    }
}
