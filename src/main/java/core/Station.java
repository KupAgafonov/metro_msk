package core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Station implements Comparable<Station> {
    private String line;
    private String name;

    @Override
    public int compareTo(Station line) {
        double thisNumber = convertLineToDouble(this.getLine());
        double lineNumber = convertLineToDouble(line.getLine());

        int lineNumbersCompare = Double.compare(thisNumber, lineNumber);
        return lineNumbersCompare == 0 ? this.getName().compareTo(line.getName()) : lineNumbersCompare;
    }

    private double convertLineToDouble(String lineNumber) {
        return Double.parseDouble(lineNumber
                .replaceAll("^D", "10")       // заменяем D на 10  - из D1 получится 101
                .replaceAll("А$|A$", ".5"));  // А или русскую А заменяем на .5 - будет 11.5
    }
}
