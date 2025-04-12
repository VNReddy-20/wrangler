package co.cask.wrangler.steps;

import co.cask.wrangler.api.Arguments;
import co.cask.wrangler.api.Directive;
import co.cask.wrangler.api.ExecutorContext;
import co.cask.wrangler.api.Row;
import co.cask.wrangler.api.UsageDefinition;
import co.cask.wrangler.api.parser.ColumnName;
import co.cask.wrangler.api.parser.ByteSize;
import co.cask.wrangler.api.parser.TimeDuration;
import co.cask.wrangler.api.parser.TokenType;

import java.util.ArrayList;
import java.util.List;

public class AggregateStatsDirective implements Directive {
  private String sizeColumn;
  private String timeColumn;
  private String totalSizeColumn;
  private String totalTimeColumn;
  private long totalBytes = 0;
  private long totalNanos = 0;

  @Override
  public UsageDefinition define() {
    UsageDefinition.Builder builder = UsageDefinition.builder("aggregate-stats");
    builder.define("size-column", TokenType.COLUMN_NAME);
    builder.define("time-column", TokenType.COLUMN_NAME);
    builder.define("total-size-column", TokenType.COLUMN_NAME);
    builder.define("total-time-column", TokenType.COLUMN_NAME);
    return builder.build();
  }

  @Override
  public void initialize(Arguments args, ExecutorContext context) {
    sizeColumn = ((ColumnName) args.value("size-column")).value();
    timeColumn = ((ColumnName) args.value("time-column")).value();
    totalSizeColumn = ((ColumnName) args.value("total-size-column")).value();
    totalTimeColumn = ((ColumnName) args.value("total-time-column")).value();
  }

  @Override
  public List<Row> execute(List<Row> rows, ExecutorContext context) {
    totalBytes = 0;
    totalNanos = 0;

    for (Row row : rows) {
      Object sizeObj = row.getValue(sizeColumn);
      if (sizeObj instanceof String) {
        ByteSize size = new ByteSize((String) sizeObj);
        totalBytes += size.getBytes();
      }

      Object timeObj = row.getValue(timeColumn);
      if (timeObj instanceof String) {
        TimeDuration time = new TimeDuration((String) timeObj);
        totalNanos += time.getNanos();
      }
    }

    List<Row> result = new ArrayList<>();
    Row output = new Row();
    output.add(totalSizeColumn, totalBytes / (1024.0 * 1024.0));
    output.add(totalTimeColumn, totalNanos / 1_000_000_000.0);
    result.add(output);
    return result;
  }

  @Override
  public void destroy() {
    // No-op
  }
}