package pl.zespolowy.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import pl.zespolowy.*;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;



@Getter
public class MainSceneController {
    @FXML
    private AnchorPane mainSceneAnchorPane;

    @FXML
    private TextArea textArea;

    @FXML
    private VBox subjectBox;

    @FXML
    private ListView subjectList;

    private Translator translator;
    private Map<String, WordSet> wordSets;
    private LanguageSet  languageSet;

    public void initialize() {

        String rootPath = System.getProperty("user.dir");

//        String languagesPath = rootPath + "/languages.json";
//        initLanguages("set1", languagesPath);
//        languageSet.print();
//
//        String wordSetPath = rootPath + "/wordsets/";
//        initWordSets(wordSetPath);
//        for (String key : wordSets.keySet()) {
//            wordSets.get(key).print();
//        }

    }

    public void setTranslator(Translator translator) {
        this.translator = translator;
    }

    public void initLanguages(String title, String path) {
        try {
            String content = Files.readString(Paths.get(path));
            languageSet = new LanguageSet(title, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initWordSets(String path) {
        wordSets = new HashMap<>();

        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            String[] fileNames = dir.list();

            if (fileNames != null) {
                for (String fileName : fileNames) {
                    try {
                        String title = fileName.split(".json")[0];
                        String content = Files.readString(Paths.get(path + fileName));

                        WordSet wordSet = new WordSet(title, content, false);
                        wordSets.put(title, wordSet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(fileName);
                }

            } else {
                System.out.println("The current directory is empty or an error occurred.");
            }
        } else {
            System.out.println("The current directory does not exist or is not a directory.");
        }
    }

    @FXML
    public void handleTranslate() {
        String wordSet = "jabłko; banan; cytryna";

        Translation translation = translator.translate(wordSet, "pl", "en");

        System.out.println("----- LIST -------- ");
        for (String str : translation.toList()) {
            System.out.println("'" + str + "'");
        }
        System.out.println();

        textArea.appendText(translation.multiLine());
    }
}
