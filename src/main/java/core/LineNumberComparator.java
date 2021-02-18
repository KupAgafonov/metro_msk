package core;

import java.util.Comparator;

public class LineNumberComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return compareLineNumber(o1, o2);
    }

    static int compareLineNumber(String l1, String l2) {
        double thisNumber = Double.parseDouble(l1.replaceAll("^D", "10")
                .replaceAll("А$|A$", ".5"));
        double lineNumber = Double.parseDouble(l2.replaceAll("^D", "10")
                .replaceAll("А$|A$", ".5"));
        return Double.compare(thisNumber, lineNumber);
    }
}

