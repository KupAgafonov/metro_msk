import core.Line;
import core.LineNumberComparator;
import core.Station;
import lombok.Data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

@Data
public class JsonResponse {

    private Map<String, List<String>> stations = new TreeMap<>(new LineNumberComparator());
    private Set<Line> lines = new TreeSet<>();
    private Set<Set<Station>> connections = new HashSet<>();

    public  void writeJson(String json, String saveTarget) throws IOException {
        List<String> jsonToString = Arrays.asList(json.split("\n"));
        Files.deleteIfExists(Paths.get(saveTarget));
        Files.write(Paths.get(saveTarget),
                jsonToString,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE);
    }
}
