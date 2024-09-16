package nextstep.cucumber.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java8.En;
import io.restassured.path.json.JsonPath;
import java.util.List;
import java.util.Map;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.acceptance.PathCommonApi;
import nextstep.subway.domain.path.dto.PathResponse;
import nextstep.subway.domain.station.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class PathStepDef implements En {

  @Autowired
  private AcceptanceContext context;

  public PathStepDef() {
  }

  @When("{string}과 {string}의 경로 탐색을 수행하면")
  public void 정상적인_지하철역_경로_탐색을_수행하면(String upStation, String downStation) {
    Long upStationId = ((StationResponse) context.store.get(upStation)).getId();
    Long downStationId = ((StationResponse) context.store.get(downStation)).getId();

    context.response = PathCommonApi.findLinePath(upStationId, downStationId, "DISTANCE");
  }

  @Then("지하철역 경로 탐색 결과가 반환된다.")
  public void 지하철역_경로_탐색_결과가_반환된다() {
    JsonPath jsonPath = context.response.jsonPath();
    List<String> list = context.response.jsonPath().getList("stations.name", String.class);
    assertThat(context.response.jsonPath().getList("stations.name", String.class)).containsExactly("남부터미널역", "양재역", "강남역", "역삼역");
    assertThat(context.response.jsonPath().getLong("distance")).isEqualTo(9);
  }

  @When("{string}을 출발역과 도착역으로 하여 경로 탐색을 수행하면")
  public void 같은_역을_출발역과_도착역으로_하여_경로_탐색을_수행하면(String sameStation) {
    Long sameStationId = ((StationResponse) context.store.get(sameStation)).getId();
    context.response = PathCommonApi.findLinePath(sameStationId, sameStationId, "DISTANCE");
  }

  @Then("지하철역 경로 탐색에 실패하고, 에러 Http Status를 반환한다.")
  public void 지하철역_경로_탐색에_실패하고_에러_http_status를_반환한다() {
    assertThat(context.response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @When("연결되지 않은 {string}과 {string}에 대해 경로 탐색을 수행하면")
  public void 연결되지_않은_두_역에_대해_경로_탐색을_수행하면(String upStation, String downStation) {
    Long upStationId = ((StationResponse) context.store.get(upStation)).getId();
    Long downStationId = ((StationResponse) context.store.get(upStation)).getId();
    context.response = PathCommonApi.findLinePath(upStationId, downStationId, "DISTANCE");
  }

  @When("등록되지 않은 {string}과 존재하는 {string}에 대하여 경로 탐색을 수행하면")
  public void 존재하지_않는_역에_대하여_경로_탐색을_수행하면(String upStation, String downStation) {
    Long upStationId = 12312L;
    Long downStationId = ((StationResponse) context.store.get(downStation)).getId();
    context.response = PathCommonApi.findLinePath(upStationId, downStationId, "DISTANCE");
  }

  @When("{string}에서 {string}까지의 최소 시간 기준으로 경로 조회를 요청")
  public void 출발역에서_도착역까지의_최소_시간_기준_경로_조회(String upStation, String downStation) {
    Long upStationId = ((StationResponse) context.store.get(upStation)).getId();
    Long downStationId = ((StationResponse) context.store.get(downStation)).getId();
    context.response = PathCommonApi.findLinePath(upStationId, downStationId, "DURATION");
  }

  @Then("최소 시간 기준 경로를 응답")
  public void 최소_시간_경로_응답(DataTable dataTable) {
    Map<String, String> data = dataTable.asMap();
    assertThat(context.response.jsonPath().getLong("transitTime")).isEqualTo(Long.parseLong(data.get("transitTime")));
  }

  @Then("총 거리와 소요 시간을 함께 응답함")
  public void 총_거리_및_소요_시간을_함께_응답() {
    PathResponse pathResponse = context.response.as(PathResponse.class);
    assertThat(pathResponse.getDistance()).isNotNull();
    assertThat(pathResponse.getTransitTime()).isNotNull();
  }
}
