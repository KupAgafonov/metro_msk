import com.fasterxml.jackson.databind.ObjectMapper;
import core.LineNumberComparator;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

@Data
public class JsonOptions {

    protected static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final Marker CONNECTIONS_COUNT = MarkerManager.getMarker("CONNECTIONS_COUNT");
    protected static final Marker PRINT_STATIONS = MarkerManager.getMarker("PRINT_STATIONS");
    protected static final Marker PRINT = MarkerManager.getMarker("PRINT");

//    private String jsonFile;
    private static final JSONParser parser = new JSONParser();
    private  String jsonFile;
    private static JSONObject jsonData;
    private static JSONObject stations;
    private static JSONArray connections;
    private static JSONArray lines;
    Map<String, Object> metroMap = new HashMap<>();

    @SneakyThrows
    public JsonOptions (String jsonFile)  {
        jsonData = (JSONObject) parser.parse(readJson(jsonFile));
        stations = (JSONObject) jsonData.get("stations");
        connections = (JSONArray) jsonData.get("connections");
        lines = (JSONArray) jsonData.get("lines");
        metroMap.put("stations", stations);
        metroMap.put("lines", lines);
        metroMap.put("connections", connections);
    }

    public void stationsCount() {
        Map<String, Integer> infoMap = new TreeMap<>(new LineNumberComparator());
        stations.keySet().forEach(line -> {
            JSONArray stationsArray = (JSONArray) stations.get(line);
            infoMap.put(line.toString(), stationsArray.size());
        });
        infoMap.forEach((key, value) -> {
            LOGGER.info(PRINT_STATIONS, "line : " + key + "  stations : " + value);
            LOGGER.info(PRINT, "line : " + key + "  stations : " + value);

        });
    }

    public void connectionsTotalCount(String file) {
        LOGGER.info(CONNECTIONS_COUNT, "connections count :" + connections.size());
        LOGGER.info(PRINT, "connections count : " + connections.size());
    }

    public  String readJson(String dataFile) {
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(dataFile));
            lines.forEach(builder::append);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder.toString();
    }

    public  void writeJson(String json, String saveTarget) throws IOException {
        List<String> jsonToString = Arrays.asList(json.split("\n"));
        Files.deleteIfExists(Paths.get(saveTarget));
        Files.write(Paths.get(saveTarget),
                jsonToString,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE);

    }

//    Write .json from ObjectMapper Method
    public void writeJsonFromObjectMapper(String saveTarget) throws IOException {
        Map<String, Object> metroMap = new HashMap<>();
        metroMap.put("stations", stations);
        metroMap.put("lines", lines);
        metroMap.put("connections", connections);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue
                (new File(String.valueOf(Paths.get(saveTarget))),
                        metroMap);
    }

}

