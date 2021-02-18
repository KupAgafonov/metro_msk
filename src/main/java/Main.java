import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.IOException;

public class Main {

    private static final String FILE_TARGET = "src/main/resources/mskMetroMap.json";
        private static final String URL = "https://www.moscowmap.ru/metro.html#lines";

    protected static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final Marker EXCEPTION_MARKER = MarkerManager.getMarker("EXCEPTION");
    protected static final Marker PRINT = MarkerManager.getMarker("PRINT");

    public static void main(String[] args) {
        try {
//             Parsing
            ParserHTML parserHTML = new ParserHTML(URL);
//             GSON
            JsonResponse jsonResponse = new JsonResponse();
            jsonResponse.setLines(parserHTML.getLines());
            jsonResponse.setStations(parserHTML.getStations());
            jsonResponse.setConnections(parserHTML.getConnections());
            String json = new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create()
                    .toJson(jsonResponse);
            jsonResponse.writeJson(json, FILE_TARGET);

            LOGGER.info(PRINT, json);
//             JSON
            JsonOptions jsonOptions = new JsonOptions(FILE_TARGET);
            jsonOptions.stationsCount();
            jsonOptions.connectionsTotalCount(FILE_TARGET);
        } catch (IOException e) {
            LOGGER.error(EXCEPTION_MARKER, (Object) e);
        }
    }
}






