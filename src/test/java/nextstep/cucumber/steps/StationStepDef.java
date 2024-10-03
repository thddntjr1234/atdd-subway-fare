package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.acceptance.StationCommonApi;
import nextstep.subway.domain.station.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StationStepDef implements En {

    @Autowired
    private AcceptanceContext context;

    public StationStepDef() {
        Given("지하철역을 생성하고", (DataTable dataTable) -> {
            List<Map<String, String>> stationNames = dataTable.asMaps();
            stationNames.forEach(e -> {
                var response = StationCommonApi.createStation(e.get("name")).as(StationResponse.class);
                context.store.put(response.getName(), response);
            });
        });

        When("지하철역을 생성하면", () -> {
            Map<String, String> params = new HashMap<>();
            params.put("name", "강남역");
            context.response = RestAssured.given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .post("/stations")
                    .then().log().all()
                    .extract();
        });

        Then("지하철역이 생성된다", () -> {
            assertThat(context.response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        });

        Then("지하철역 목록 조회 시 생성한 역을 찾을 수 있다", () -> {
            List<String> stationNames =
                    RestAssured.given().log().all()
                            .when().get("/stations")
                            .then().log().all()
                            .extract().jsonPath().getList("name", String.class);
            assertThat(stationNames).containsAnyOf("강남역");
        });
    }

}
