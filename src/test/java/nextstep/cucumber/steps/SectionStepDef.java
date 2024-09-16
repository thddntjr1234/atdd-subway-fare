package nextstep.cucumber.steps;


import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import java.util.List;
import java.util.Map;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.acceptance.LineCommonApi;
import nextstep.subway.domain.section.dto.SectionCreateRequest;
import nextstep.subway.domain.station.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;

public class SectionStepDef {

  @Autowired
  private AcceptanceContext context;

  public SectionStepDef() {
  }

  @Given("구간을 추가한다")
  public void 지하철_구간_추가(DataTable dataTable) {
    List<Map<String, String>> maps = dataTable.asMaps();
    maps.forEach(e -> {
      var request = new SectionCreateRequest(
          ((StationResponse) context.store.get(e.get("upStation"))).getId(),
          ((StationResponse) context.store.get(e.get("downStation"))).getId(),
          Integer.parseInt(e.get("distance")),
          Integer.parseInt(e.get("transitTime")));

      var lineId = ((Long) context.store.get(e.get("name")));
      LineCommonApi.addSection(lineId, request);
    });
  }

}
