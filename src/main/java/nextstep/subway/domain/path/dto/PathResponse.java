package nextstep.subway.domain.path.dto;

import nextstep.subway.domain.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private Long distance;
    private Long transitTime;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, Long distance, Long transitTime) {
        this.stations = stations;
        this.distance = distance;
        this.transitTime = transitTime;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getTransitTime() {
        return transitTime;
    }
}
