package nextstep.subway.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("요금 조회기능 단위 테스트")
public class FareTest {

    @DisplayName("거리 기반 정책 요금 계산")
    @Nested
    class distanceFareTest {
        @DisplayName("10km 이내는 기본 운임만 부과된다.")
        @Test
        void distanceFareCase1() {
            Fare fare = new Fare();
            fare.calculateDistanceBasedFare(9);
            assertThat(fare.getFare()).isEqualTo(1250L);
        }

        @DisplayName("10km 초과 ~ 50km까지 5km마다 100원이 추가 부과된다.")
        @Test
        void distanceFareCase2() {
            Fare fare = new Fare();
            fare.calculateDistanceBasedFare(15);
            assertThat(fare.getFare()).isEqualTo(1350L);
            fare.calculateDistanceBasedFare(35);
            assertThat(fare.getFare()).isEqualTo(1750L);
            fare.calculateDistanceBasedFare(50);
            assertThat(fare.getFare()).isEqualTo(2050L);
        }

        @DisplayName("50Km 초과 시 8km마다 100이 추가 부과된다.")
        @Test
        void distanceFareCase3() {
            Fare fare = new Fare();
            fare.calculateDistanceBasedFare(50);
            assertThat(fare.getFare()).isEqualTo(2050L);
            fare.calculateDistanceBasedFare(55);
            assertThat(fare.getFare()).isEqualTo(2150L);
            fare.calculateDistanceBasedFare(58);
            assertThat(fare.getFare()).isEqualTo(2150L);
            fare.calculateDistanceBasedFare(59);
            assertThat(fare.getFare()).isEqualTo(2250L);
        }
    }

    //@DisplayName("노선별 추가요금 기반 정책 요금 계산")
    //@Nested
    //class additionalFareTest {
    //    @DisplayName("경로 조회 시 추가 요금이 있는 노선이 포함되는 경우 가장 높은 추가 요금을 가진 노선의 요금만 반영된다.")
    //    @Test
    //    void additionalFare() {
    //    }
    //}
}
