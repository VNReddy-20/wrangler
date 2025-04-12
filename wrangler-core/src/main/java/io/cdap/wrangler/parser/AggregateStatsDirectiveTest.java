package co.cask.wrangler.steps;

import co.cask.wrangler.TestingRig;
import co.cask.wrangler.api.Row;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AggregateStatsDirectiveTest {

  @Test
  public void testAggregateStats() throws Exception {
    List<Row> rows = new ArrayList<>();
    rows.add(new Row("data_transfer_size", "10KB").add("response_time", "150ms"));
    rows.add(new Row("data_transfer_size", "1.5MB").add("response_time", "2.1s"));
    rows.add(new Row("data_transfer_size", "1024B").add("response_time", "1ms"));

    String[] recipe = new String[] {
      "aggregate-stats :data_transfer_size :response_time total_size_mb total_time_sec"
    };

    List<Row> results = TestingRig.execute(recipe, rows);

    Assert.assertEquals(1, results.size());
    Row result = results.get(0);
    double totalSizeMb = (10 * 1024 + 1.5 * 1024 * 1024 + 1024) / (1024.0 * 1024.0);
    double totalTimeSec = (150 * 1_000_000 + 2.1 * 1_000_000_000 + 1 * 1_000_000) / 1_000_000_000.0;
    Assert.assertEquals(totalSizeMb, result.getValue("total_size_mb"), 0.001);
    Assert.assertEquals(totalTimeSec, result.getValue("total_time_sec"), 0.001);
  }
}