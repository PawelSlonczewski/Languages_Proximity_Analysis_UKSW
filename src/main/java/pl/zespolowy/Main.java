package pl.zespolowy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;
import pl.zespolowy.Business.Algorithm.*;
import pl.zespolowy.Controllers.MainSceneController;

import java.io.IOException;
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

        WordSetsTranslation wst = new WordSetsTranslation();
        WordSetsRegrouper wordSetsRegroup = new WordSetsRegrouper(wst);
        LanguageSimilarityCalculator languageProximity = new LanguageSimilarityCalculator(wordSetsRegroup);
        languageProximity.countingProximityForWordInDifferentLanguagesAndPuttingResultToLanguageProximityResult();
        WordsProximityNormalizer wordsProximityNormalizer = new WordsProximityNormalizer(languageProximity);
        System.out.println(wordsProximityNormalizer.getCountedProximityBetweenWords());
//        Map<String, Map<String, LanguageProximityResult>> proximityBetweenTwoLanguagesMapByTopic = languageProximity.getProximityBetweenTwoLanguagesMapByTopic();
//        proximityBetweenTwoLanguagesMapByTopic.entrySet().forEach(a -> System.out.println(a.toString()));



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