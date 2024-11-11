package pl.zespolowy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;
import pl.zespolowy.Business.Algorithm.LanguageTranslationAndSimilarityCalculator;
import pl.zespolowy.Business.Algorithm.LanguageProximityResult;
import pl.zespolowy.Controllers.MainSceneController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException, NoSuchFieldException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main-scene.fxml"));
        Parent root = fxmlLoader.load();
        MainSceneController controller = fxmlLoader.getController();

        Translator translator = new Translator();
        controller.setTranslator(translator);





        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("Windows");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        LanguageTranslationAndSimilarityCalculator languageProximity = new LanguageTranslationAndSimilarityCalculator();
        languageProximity.getLanguages();
        List<Language> languageList = languageProximity.getLanguageList();
//        Map<String, ProximityBetweenTwoLanguages> proximityBetweenTwoLanguages = languageProximity.makeSetOfProximityBetweenTwoLanguages(languageList);
        languageProximity.countingProximityForWordInDifferentLanguagesAndPuttingResultToLanguageProximityResult(languageList);
        Map<String, LanguageProximityResult> proximityBetweenTwoLanguagesMap = languageProximity.getProximityBetweenTwoLanguagesMap();
        proximityBetweenTwoLanguagesMap.values().forEach(a -> System.out.println(a.toString()));

//        Map<Language, Word> words = languageProximity.translateWordToDifferentLanguages(new Word("Apple"), languageList);
//        words.values().stream()
//                .map(Word::getText)
//                .forEach(System.out::println);


        //String result = translator.translate("dupa", "pl", "en");
        //System.out.println(result);

        launch();
        //TodoClient todoClient = new TodoClient("technology");
//
        //Set<String> wordSet = todoClient.findAll();
        //for (String word : wordSet) {
        //    System.out.println(word);
        //}
//
        //System.out.println(wordSet.size());


    }


}