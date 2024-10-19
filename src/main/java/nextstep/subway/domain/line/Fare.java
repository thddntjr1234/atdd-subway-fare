package nextstep.subway.domain.line;

import java.util.List;

public class Fare {
    private static final long DEFAULT_FARE = 1250L;

    private Long fare;

    public Fare() {
        this.fare = DEFAULT_FARE;
    }

    public Fare(Long fare) {
        this.fare = fare;
    }

    public void calculateDistanceBasedFare(long distance) {
        long totalFare = this.fare;
        if (distance <= 10) {
            return;
        }

        if (distance <= 50) {
            long until50km = distance - 10;
            this.fare = totalFare + (long) ((Math.ceil((until50km - 1) / 5) + 1) * 100);
            return;
        }

        // 50km까지 요금
        long until50km = 40;
        totalFare += (long) ((Math.ceil((until50km - 1) / 5) + 1) * 100);

        // 50km 초과 요금
        long over50km = distance - 50;
        this.fare = totalFare + (long) ((Math.ceil((over50km - 1) / 8) + 1) * 100);
    }

    public void calculateDistanceBasedFare(long distance, List<Line> lines) {
        calculateDistanceBasedFare(distance);
        calculateHighestAdditionalFare(lines);
    }

    private void calculateHighestAdditionalFare(List<Line> lines) {
        Long highestAdditionalFare = lines.stream()
                .map(Line::getAdditionalFare)
                .max(Long::compare)
                .get();

        this.fare += highestAdditionalFare;
    }


    public Long getFare() {
        return fare;
    }
}
