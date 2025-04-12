package co.cask.wrangler.api.parser;

import io.java.co.cask.wrangler.api.parser.Token;

public class ByteSize implements Token {
  private final String raw;
  private final long bytes;

  public ByteSize(String value) {
    this.raw = value;
    this.bytes = parseBytes(value);
  }

  @Override
  public String raw() {
    return raw;
  }

  @Override
  public TokenType type() {
    return TokenType.BYTE_SIZE;
  }

  public long getBytes() {
    return bytes;
  }

  private long parseBytes(String value) {
    String cleaned = value.toLowerCase().replaceAll("[^0-9.]", "");
    String unit = value.toLowerCase().replaceAll("[0-9.]", "");
    double number = Double.parseDouble(cleaned);
    switch (unit) {
      case "b": return (long) number;
      case "kb": return (long) (number * 1024);
      case "mb": return (long) (number * 1024 * 1024);
      case "gb": return (long) (number * 1024 * 1024 * 1024);
      case "tb": return (long) (number * 1024 * 1024 * 1024 * 1024);
      default: throw new IllegalArgumentException("Unknown byte unit: " + unit);
    }
  }
}