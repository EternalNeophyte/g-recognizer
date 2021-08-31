package edu.psuti.alexandrov.grecognizer;

import java.util.Objects;
import java.util.concurrent.*;

public class AnalysisSheduler {

    private ForkJoinTask<ResultsTable<?, ?>> task;
    private ForkJoinPool pool;

    public ForkJoinTask<ResultsTable<?, ?>> getTask() {
        return task;
    }

    public ForkJoinPool getPool() {
        return pool;
    }

    private boolean shedulerNotReady() {
        return taskNotDefined() || serviceNotDefined();
    }

    private boolean serviceNotDefined() {
        return Objects.isNull(pool);
    }

    private boolean taskNotDefined() {
        return Objects.isNull(task);
    }

    public ForkJoinTask<ResultsTable<?, ?>> call() {
        if(shedulerNotReady()) {
            throw new ShedulerNotReadyException();
        }
        return pool.submit(task);
    }

    public static Builder builder() {
        return new AnalysisSheduler().new Builder();
    }

    public class Builder {

        public Builder task(ForkJoinTask<ResultsTable<?, ?>> task) {
            AnalysisSheduler.this.task = task;
            return this;
        }

        public Builder service(ForkJoinPool pool) {
            AnalysisSheduler.this.pool = pool;
            return this;
        }

        public Builder serviceByDefault() {
            return service(ForkJoinPool.commonPool());
        }

        public AnalysisSheduler build() {
            if(serviceNotDefined()) {
                serviceByDefault();
            }
            return AnalysisSheduler.this;
        }
    }
}
