package edu.psuti.alexandrov.grecognizer;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AnalysisSheduler {

    private Callable<ResultTable> task;
    private ExecutorService service;

    public Callable<ResultTable> getTask() {
        return task;
    }

    public ExecutorService getService() {
        return service;
    }

    private boolean shedulerNotReady() {
        return taskNotDefined() || serviceNotDefined();
    }

    private boolean serviceNotDefined() {
        return Objects.isNull(service);
    }

    private boolean taskNotDefined() {
        return Objects.isNull(task);
    }

    public Future<ResultTable> call() {
        if(shedulerNotReady()) {
            throw new ShedulerNotReadyException();
        }
        return service.submit(task);
    }

    public static Builder builder() {
        return new AnalysisSheduler().new Builder();
    }

    public class Builder {

        public Builder task(Callable<ResultTable> task) {
            AnalysisSheduler.this.task = task;
            return this;
        }

        public Builder service(ExecutorService service) {
            AnalysisSheduler.this.service = service;
            return this;
        }

        public Builder serviceByDefault() {
            return service(Executors.newCachedThreadPool());
        }

        public AnalysisSheduler build() {
            if(serviceNotDefined()) {
                serviceByDefault();
            }
            return AnalysisSheduler.this;
        }
    }
}
