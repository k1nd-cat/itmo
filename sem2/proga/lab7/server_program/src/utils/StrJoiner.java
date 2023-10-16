package utils;

import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class StrJoiner extends RecursiveTask<String> {

    private List<String> values;
    private String delimeter;

    public StrJoiner(List<String> values, String delimeter) {
        this.values = values;
        this.delimeter = delimeter;
    }

    protected String compute() {
        if (values.size() == 1) {
            return values.get(0);
        }
        if (values.size() <= 2) {
            return values.stream().collect(Collectors.joining(delimeter));
        }
        StrJoiner strJoiner1 = new StrJoiner(values.subList(0, values.size() / 2), delimeter);
        StrJoiner strJoiner2 = new StrJoiner(values.subList(values.size() / 2, values.size()), delimeter);
        strJoiner1.fork();
        strJoiner2.fork();
        return strJoiner1.join().concat(delimeter).concat(strJoiner2.join());
    }
}
