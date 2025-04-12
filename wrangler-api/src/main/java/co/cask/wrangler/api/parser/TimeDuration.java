package co.cask.wrangler.api.parser;

import io.java.co.cask.wrangler.api.parser.Token;

public class TimeDuration implements Token {
  private final String raw;
  private final long nanos;

  public TimeDuration(String value) {
    this.raw = value;
    this.nanos = parseNanos(value);
  }

  @Override
  public String raw() {
    return raw;
  }

  @Override
  public TokenType type() {
    return TokenType.TIME_DURATION;
  }

  public long getNanos() {
    return nanos;
  }

  private long parseNanos(String value) {
    String cleaned = value.toLowerCase().replaceAll("[^0-9.]", "");
    String unit = value.toLowerCase().replaceAll("[0-9.]", "");
    double number = Double.parseDouble(cleaned);
    switch (unit) {
      case "ns": return (long) number;
      case "us": return (long) (number * 1_000);
      case "ms": return (long) (number * 1_000_000);
      case "s": return (long) (number * 1_000_000_000);
      case "m": return (long) (number * 60 * 1_000_000_000);
      case "h": return (long) (number * 3600 * 1_000_000_000);
      default: throw new IllegalArgumentException("Unknown time unit: " + unit);
    }
  }
}