package utils;

import java.util.List;
import java.util.concurrent.RecursiveTask;

public class SumCounter extends RecursiveTask<Long> {

    private List<Long> values;

    public SumCounter(List<Long> values) {
        this.values = values;
    }

    protected Long compute() {
        if (values.size() == 1) {
            return values.get(0);
        }
        if (values.size() <= 2) {
            return values.stream().reduce(0L, Long::sum);
        }
        SumCounter avgCounter1 = new SumCounter(values.subList(0, values.size() / 2));
        SumCounter avgCounter2 = new SumCounter(values.subList(values.size() / 2, values.size()));
        avgCounter1.fork();
        avgCounter2.fork();
        return avgCounter1.join() + avgCounter2.join();
    }
}
