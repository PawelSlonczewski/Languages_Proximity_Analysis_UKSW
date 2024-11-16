package pl.zespolowy.Business.Algorithm;

import lombok.Getter;
import lombok.Setter;
import pl.zespolowy.WordSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Getter
@Setter
public class WordSetsReader {
    private final Optional<WordSet> wordSet;

    public WordSetsReader(String path, String filename) throws IOException {
        this.wordSet = setWordSet(path, filename);
    }

    public Optional<WordSet> setWordSet(String path, String fileName) {
        WordSet wordSet = null;
        try {
            String title = fileName.split(".json")[0];
            String content = Files.readString(Paths.get(path + fileName));
            wordSet = new WordSet(title, content, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(wordSet);
    }
}
