package nextstep.cucumber.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.LineCommonApi;
import nextstep.subway.acceptance.PathCommonApi;
import nextstep.subway.acceptance.StationCommonApi;
import nextstep.subway.domain.line.dto.LineCreateRequest;
import nextstep.subway.domain.section.dto.SectionCreateRequest;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;

public class PathStepDef implements En {
  private Long 교대역;
  private Long 강남역;
  private Long 양재역;
  private Long 역삼역;
  private Long 남부터미널역;
  private Long 석남역;
  private Long 이호선;
  private Long 신분당선;
  private Long 삼호선;

  ExtractableResponse<Response> response;

  public PathStepDef() {
    setUp();
  }

  /**
   * 교대역    --- *2호선* ---   강남역
   * |                        |
   * *3호선*                   *신분당선*
   * |                        |
   * 남부터미널역  --- *3호선* ---   양재
   */
  public void setUp() {
    교대역 = StationCommonApi.createStation("교대역").jsonPath().getLong("id");
    강남역 = StationCommonApi.createStation("강남역").jsonPath().getLong("id");
    역삼역 = StationCommonApi.createStation("역삼역").jsonPath().getLong("id");
    양재역 = StationCommonApi.createStation("양재역").jsonPath().getLong("id");
    남부터미널역 = StationCommonApi.createStation("남부터미널역").jsonPath().getLong("id");
    석남역 = StationCommonApi.createStation("석남역").jsonPath().getLong("id");

    이호선 = LineCommonApi.createLine(new LineCreateRequest("2호선", "green", 교대역, 강남역, 10)).jsonPath().getLong("id");
    신분당선 = LineCommonApi.createLine(new LineCreateRequest("신분당선", "red", 강남역, 양재역, 4)).jsonPath().getLong("id");
    삼호선 = LineCommonApi.createLine(new LineCreateRequest("3호선", "orange", 교대역, 남부터미널역, 6)).jsonPath().getLong("id");

    LineCommonApi.addSection(이호선, new SectionCreateRequest(강남역, 역삼역, 2));
    LineCommonApi.addSection(삼호선, new SectionCreateRequest(남부터미널역, 양재역, 3));
  }

  @When("정상적인 지하철역 경로 탐색을 수행하면")
  public void 정상적인_지하철역_경로_탐색을_수행하면() {
    response = PathCommonApi.findLinePath(남부터미널역, 역삼역);
  }

  @Then("지하철역 경로 탐색 결과가 반환된다.")
  public void 지하철역_경로_탐색_결과가_반환된다() {
    assertThat(response.jsonPath().getList("stations.name", String.class)).containsExactly("남부터미널역", "양재역", "강남역", "역삼역");
    assertThat(response.jsonPath().getLong("distance")).isEqualTo(9);
  }

  @When("같은 역을 출발역과 도착역으로 하여 경로 탐색을 수행하면")
  public void 같은_역을_출발역과_도착역으로_하여_경로_탐색을_수행하면() {
    response = PathCommonApi.findLinePath(남부터미널역, 남부터미널역);
  }

  @Then("지하철역 경로 탐색에 실패하고, 에러 Http Status를 반환한다.")
  public void 지하철역_경로_탐색에_실패하고_에러_http_status를_반환한다() {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @When("연결되지 않은 두 역에 대해 경로 탐색을 수행하면")
  public void 연결되지_않은_두_역에_대해_경로_탐색을_수행하면() {
    response = PathCommonApi.findLinePath(석남역, 남부터미널역);
  }

  @When("존재하지 않는 역에 대하여 경로 탐색을 수행하면")
  public void 존재하지_않는_역에_대하여_경로_탐색을_수행하면() {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }
}
