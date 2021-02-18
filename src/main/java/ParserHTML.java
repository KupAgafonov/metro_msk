import core.Line;
import core.LineNumberComparator;
import core.Station;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class ParserHTML {

    private final TreeMap<String, List<String>> stations = new TreeMap<>(new LineNumberComparator());
    private final TreeSet<Line> lines = new TreeSet<>();
    private final Set<Set<Station>> connections = new HashSet<>();

    Document metropolitan;

    public ParserHTML(String URL) throws IOException {
        metropolitan = Jsoup.connect(URL).userAgent("Chrome").maxBodySize(0).get();
    }

    public Set<Line> getLines() {
        Elements lineSelect = metropolitan.select(".js-metro-line");
        lineSelect.forEach(element ->
                lines.add(new Line(element.attr("data-line"), element.text())));
        return lines;
    }


    public TreeMap<String, List<String>> getStations() {
        Elements stationsElements = metropolitan.select(".js-metro-stations");
        stationsElements.forEach(element ->
            stations.put(element.attr("data-line"),
                    element.select("span.name").eachText()));
        return stations;
    }

    public Set<Set<Station>> getConnections () {
        lines.forEach(line -> {
            Elements stationInfo = metropolitan.select(".js-metro-stations[data-line = " + line.getNumber() + "]").select("a");
            stationInfo.forEach(element -> {
                if (element.childrenSize() >= 3)
                    connections.add(addConnectionsOnLine(element, line.getNumber()));
            });
        });
        return connections;
    }

    static Set<Station> addConnectionsOnLine(Element stationInfo, String lineNumber) {
        Set<Station> connections = new TreeSet<>();
        connections.add(new Station(lineNumber, stationInfo.select("span.name").text()));
        for (int i = 2; i < stationInfo.childrenSize(); i++) {
            String line = getLineNumberFromElement(stationInfo.children().get(i).attr("class"));
            String name = getNameStationFromElement(stationInfo.children().get(i).attr("title"));
            connections.add(new Station(line, name));
        }
//        TreeSet<Station> connectionsSort = connections.stream().sorted().collect(Collectors.toCollection(TreeSet::new));
        return connections;
    }

    static String getLineNumberFromElement(String string) {  //inputString = "t-icon-metroln ln-14"
        int tirLines = string.lastIndexOf("-") + 1;
        return string.substring(tirLines);  //return 14
    }

    static String getNameStationFromElement(String string) { // "переход на станцию «Петровско-Разумовская» Люблинско-Дмитровской линии"
        return string.replaceAll("(.* «)([А-яё]*\\s?-?[А-яё]*\\s?[А-яё]*)(» .+)", "$2"); // Петровско-Разумовская
    }

}
