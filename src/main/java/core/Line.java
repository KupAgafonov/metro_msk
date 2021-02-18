package core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Line implements Comparable<Line> {

    private String number;
    private String name;

    @Override
    public int compareTo(Line line) {
        return this.getNumber().compareTo(line.getNumber());
    }
}

