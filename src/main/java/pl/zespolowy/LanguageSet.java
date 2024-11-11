package pl.zespolowy;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LanguageSet {
    private String title;
    private List<Language> languages;

    public LanguageSet(String title, String jsonString) {
        this.title = title;
        Deserialize(jsonString);
    }

    private void Deserialize(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            languages = objectMapper.readValue(jsonString, objectMapper.getTypeFactory().constructCollectionType(List.class, Language.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void addLanguage(Language language) {
        languages.add(language);
    }

    // test
    public void print() {
        System.out.println("---- Languages in set \"" + title + "\": ----");
        for (Language l : languages) {
            System.out.println(l.getName());
        }
        System.out.println();
    }
}
