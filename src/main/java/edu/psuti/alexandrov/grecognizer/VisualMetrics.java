package edu.psuti.alexandrov.grecognizer;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class VisualMetrics {

    private AtomicReference<BigDecimal> percentage;
    private AtomicInteger pixelCount;

    public AtomicReference<BigDecimal> getPercentage() {
        return percentage;
    }

    public AtomicInteger getPixelCount() {
        return pixelCount;
    }

    @Override
    public String toString() {
        return String.format("%10f or %d pixels",
                percentage.get(),
                pixelCount.get());
    }

    public static Builder builder() {
        return new VisualMetrics().new Builder();
    }

    public class Builder {

        public Builder percentage(BigDecimal percentage) {
            VisualMetrics.this.percentage = new AtomicReference<>(percentage);
            return this;
        }

        public Builder pixelCount(AtomicInteger pixelCount) {
            VisualMetrics.this.pixelCount = pixelCount;
            return this;
        }

        public Builder percentageFromInt(int percentage) {
            return percentage(BigDecimal.valueOf(percentage));
        }

        public Builder pixelCountFromInt(int pixelCount) {
            return pixelCount(new AtomicInteger(pixelCount));
        }

        public VisualMetrics build() {
            return VisualMetrics.this;
        }
    }
}
