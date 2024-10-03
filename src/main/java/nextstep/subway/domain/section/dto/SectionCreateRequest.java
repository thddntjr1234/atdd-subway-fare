package nextstep.subway.domain.section.dto;

import nextstep.subway.domain.section.Section;

public class SectionCreateRequest {
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int transitTime;

    public SectionCreateRequest() {
    }

    public SectionCreateRequest(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public SectionCreateRequest(Long upStationId, Long downStationId, int distance, int transitTime) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.transitTime = transitTime;
    }

    public static SectionCreateRequest of(Section section) {
        return new SectionCreateRequest(section.getUpwardStation().getId(),
                section.getDownwardStation().getId(),
                section.getDistance(),
                section.getTransitTime());
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getTransitTime() {
        return transitTime;
    }
}
