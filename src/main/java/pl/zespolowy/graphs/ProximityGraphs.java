package pl.zespolowy.graphs;

import lombok.Getter;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import pl.zespolowy.Language;

import java.util.*;

// TODO is mapPreparedForJson the best name for variable? (in this file)

@Getter
public class ProximityGraphs {

    // TODO create graph for overall language proximity
    private final SingleGraph overallLanguageProximityGraph = new SingleGraph("Overall Language Proximity Graph");
    private final SingleGraph themesLanguageProximityGraphs;
    private final String rootPathToFlags = "src/main/resources/flags/fullnames/";


    // TODO how to get selected languages and topics lists?
    // TODO change last argument to have language instead of string
    public ProximityGraphs(final List<Language> selectedLanguages, final List<String> selectedThemes,
                           final Map<String, Map<String, Map<String, Double>>> mapPreparedForJson) {
        // creating graphs only for selected languages
        this.themesLanguageProximityGraphs = createThemeGraphsShortcut(/* selectedLanguages, selectedThemes , */ mapPreparedForJson);
    }


    // TODO is this map the best option to retrieve data?
    private SingleGraph createThemeGraphs(/* List<Language> selectedLanguages, List<String> themes ,*/
                                                final Map<String, Map<Language, Map<Language, Double>>> data) {

        SingleGraph graph = new SingleGraph("Theme Graphs");


        SpriteManager spriteManager = new SpriteManager(graph);
        for (var themeSet : data.entrySet()) {
            String theme = themeSet.getKey();
            // prob not needed
//            graph.setStrict(false);
//            graph.setAutoCreate(true);
            for (var mainLanguageSet : themeSet.getValue().entrySet()) {
                String mainLanguage = mainLanguageSet.getKey().getCode();
                if (graph.getNode(theme + "_" + mainLanguage) == null) {
                    Node node = graph.addNode(theme + "_" + mainLanguage);
                    node.setAttribute("ui.label", mainLanguage);
                }
                StringBuilder edgeName = new StringBuilder(theme + "_" + mainLanguage);
                for (var subLanguageSet : mainLanguageSet.getValue().entrySet()) {
                    String subLanguage = subLanguageSet.getKey().getCode();
                    if (graph.getNode(theme + "_" + subLanguage) != null) {
                        if (ifEdgeExists(theme, mainLanguage, subLanguage, graph)) {
                            continue;
                        }
                    } else {
                        Node node = graph.addNode(theme + "_" + subLanguage);
                        node.setAttribute("ui.label", subLanguage);
                    }
                    edgeName.append("-").append(subLanguage);
                    Edge e = graph.addEdge(edgeName.toString(), theme + "_" + mainLanguage, theme + "_" + subLanguage);
                    e.setAttribute("layout.weight", subLanguageSet.getValue());
                    e.setAttribute("ui.label", String.format("%.2f", subLanguageSet.getValue()));


                    int targetIndex = edgeName.indexOf(String.valueOf('-'));
                    edgeName.delete(targetIndex, edgeName.length());
                }
            }
            Node nodeForSprite = graph.nodes().filter(node -> node.getId().contains(theme))
                    .findAny().orElseThrow(() -> new NoSuchElementException("No node found matching the theme!"));

            Sprite s = spriteManager.addSprite(theme);
            s.attachToNode(nodeForSprite.getId());
            s.setPosition(50, 200, 0);
            s.setAttribute("ui.style", "size: 10px;");
        }


        // For better visualization
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");

        return graph;
    }

    private SingleGraph createThemeGraphsShortcut(/* List<Language> selectedLanguages, List<String> themes ,*/
            final Map<String, Map<String, Map<String, Double>>> data) {
        String level1 = "size: 5px; fill-color: #2eff00;"; // [0.0 - 0.2)
        String level2 = "size: 4px; fill-color: #94ff00;"; // [0.2 - 0.4)
        String level3 = "size: 3px; fill-color: #f0f500;"; // [0.4 - 0.6)
        String level4 = "size: 2px; fill-color: #ff9e00;"; // [0.6 - 0.8)
        String level5 = "size: 1px; fill-color: #ff3600;"; // [0.8 - 1.0]

        SingleGraph graph = new SingleGraph("Theme Graphs");

        graph.setAttribute("ui.stylesheet", "graph { fill-color: #ddcccc; }");

        SpriteManager spriteManager = new SpriteManager(graph);
        for (var themeSet : data.entrySet()) {
            String theme = themeSet.getKey();
            // prob not needed
//            graph.setStrict(false);
//            graph.setAutoCreate(true);
            for (var mainLanguageSet : themeSet.getValue().entrySet()) {
                String mainLanguage = mainLanguageSet.getKey();
                if (graph.getNode(theme + "_" + mainLanguage) == null) {
                    Node node = graph.addNode(theme + "_" + mainLanguage);
                    // node.setAttribute("ui.label", mainLanguage);
                    String url = rootPathToFlags + mainLanguage;
                    // had to add javafx.swing to libs
                    node.setAttribute("ui.style",
                            "size: 0.5gu; shape: circle; fill-mode: image-scaled; fill-image: url('src/main/resources/flags/fullnames/" + mainLanguage.toLowerCase() + ".png');");
                }
                StringBuilder edgeName = new StringBuilder(theme + "_" + mainLanguage);
                for (var subLanguageSet : mainLanguageSet.getValue().entrySet()) {
                    String subLanguage = subLanguageSet.getKey();
                    if (graph.getNode(theme + "_" + subLanguage) != null) {
                        if (ifEdgeExists(theme, mainLanguage, subLanguage, graph)) {
                            continue;
                        }
                    } else {
                        Node node = graph.addNode(theme + "_" + subLanguage);
                        // node.setAttribute("ui.label", subLanguage);
                        node.setAttribute("ui.style",
                                "size: 0.5gu; shape: circle; fill-mode: image-scaled; fill-image: url('src/main/resources/flags/fullnames/" + subLanguage.toLowerCase() + ".png');");
                    }
                    edgeName.append("-").append(subLanguage);
                    Edge e = graph.addEdge(edgeName.toString(), theme + "_" + mainLanguage, theme + "_" + subLanguage);
                    e.setAttribute("layout.weight", subLanguageSet.getValue() * 6);
                    e.setAttribute("proximity", subLanguageSet.getValue());
                    e.setAttribute("ui.label", String.format("%.2f", subLanguageSet.getValue()));

                    int targetIndex = edgeName.indexOf(String.valueOf('-'));
                    edgeName.delete(targetIndex, edgeName.length());
                }
            }

            graph.edges().forEach(edge -> {
                double proximity = edge.getNumber("proximity");
                if (proximity < 0.2) {
                    edge.setAttribute("ui.style", level1);
                } else if (proximity < 0.4) {
                    edge.setAttribute("ui.style", level2);
                } else if (proximity < 0.6) {
                    edge.setAttribute("ui.style", level3);
                } else if (proximity < 0.8) {
                    edge.setAttribute("ui.style", level4);
                } else if (proximity <= 1.0) {
                    edge.setAttribute("ui.style", level5);
                } else {
                    throw new RuntimeException("Languages proximity too large!");
                }
            });

            Node nodeForSprite = graph.nodes().filter(node -> node.getId().contains(theme))
                    .findAny().orElseThrow(() -> new NoSuchElementException("No node found matching the theme!"));

            Sprite s = spriteManager.addSprite(theme);
            s.attachToNode(nodeForSprite.getId());
            s.setPosition(StyleConstants.Units.GU, 2, 1, 3);
            s.setAttribute("ui.style", "size: 0px; text-size: 15px; text-color: blue; text-style: bold;");
            s.setAttribute("ui.label", s.getId());

        }

        // For better visualization
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");

        return graph;
    }

    private boolean ifEdgeExists(final String theme, final String language1, final String language2, SingleGraph graph) {
        return graph.getEdge(theme + "_" + language1 + "-" + language2) != null
               || graph.getEdge(theme + "_" + language2 + "-" + language1) != null;
    }
}
