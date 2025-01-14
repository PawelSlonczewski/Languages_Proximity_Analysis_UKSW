package pl.zespolowy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxDefaultView;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.javafx.FxGraphRenderer;
import org.graphstream.ui.layout.Layout;
import java.io.File;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import pl.zespolowy.Business.Algorithm.*;
import pl.zespolowy.Controllers.MainSceneController;
import pl.zespolowy.graphs.ProximityGraphs;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException, NoSuchFieldException {

//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main-scene.fxml"));
//        Parent root = fxmlLoader.load();
//        MainSceneController controller = fxmlLoader.getController();
//
//        Translator translator = new Translator();
//        controller.setTranslator(translator);


        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Map<String, Map<String, Double>>> result;

        // Just for performance purpose
        try {
            TypeReference<Map<String, Map<String, Map<String, Double>>>> typeRef
                    = new TypeReference<Map<String, Map<String, Map<String, Double>>>>() {
            };

            result = objectMapper.readValue(new File("src/main/resources/results.json"), typeRef);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ProximityGraphs proximityGraphs = new ProximityGraphs(null, null,
                result);

        SingleGraph graph = proximityGraphs.getThemesLanguageProximityGraphs();

        StackPane graphPane = new StackPane();

        FxViewer view = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        view.enableAutoLayout();
        FxViewPanel panel = (FxViewPanel) view.addView(FxViewer.DEFAULT_VIEW_ID, new FxGraphRenderer());
        //FxDefaultView panel = (FxDefaultView) view.addDefaultView(false);
        graphPane.getChildren().addAll(panel);

        panel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    // reset panel settings
                    panel.getCamera().resetView();
                    return;
                }
                double x = mouseEvent.getSceneX();
                double y = mouseEvent.getSceneY();

                Point3 graphCoordinates = panel.getCamera().transformPxToGu(x, y);

                panel.getCamera().setViewCenter(graphCoordinates.x, graphCoordinates.y, 0);
                panel.getCamera().setViewPercent(0.3);
            }
        });

        Scene scene = new Scene(graphPane, 1280, 720);

        stage.setTitle("Windows");
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            view.close();
        });

        stage.show();

        // TODO program doesnt close, probably viewers, make not connected edges push each-other less
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.setProperty("org.graphstream.ui", "javafx");

//        Map<String, Map<String, LanguageProximityResult>> proximityBetweenTwoLanguagesMapByTopic = languageProximity.getProximityBetweenTwoLanguagesMapByTopic();
//        proximityBetweenTwoLanguagesMapByTopic.entrySet().forEach(a -> System.out.println(a.toString()));

//        ObjectMapper objectMapper = new ObjectMapper();
//
//        WordSetsTranslation wst = new WordSetsTranslation();
//        WordSetsRegrouper wordSetsRegroup = new WordSetsRegrouper(wst);
//        LanguageSimilarityCalculator languageProximity = new LanguageSimilarityCalculator(wordSetsRegroup);
//        languageProximity.countingProximityForWordInDifferentLanguagesAndPuttingResultToLanguageProximityResult();
//        String mapAsString = objectMapper.writeValueAsString(languageProximity.getProximityBetweenTwoLanguagesMapByTopic());
//        System.out.println(mapAsString);
        // WordsProximityNormalizer wordsProximityNormalizer = new WordsProximityNormalizer(languageProximity, wst);
        // ProximityResultJSONExporter proximityResultJSONExporter = new ProximityResultJSONExporter(wordsProximityNormalizer);
        // proximityResultJSONExporter.createJson();




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