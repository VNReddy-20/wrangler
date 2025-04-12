package co.cask.wrangler.parser;

import co.cask.wrangler.api.parser.ByteSize;
import co.cask.wrangler.api.parser.TimeDuration;
import org.junit.Assert;
import org.junit.Test;

public class ByteSizeTimeDurationTest {

  @Test
  public void testByteSizeParsing() {
    ByteSize size1 = new ByteSize("10KB");
    Assert.assertEquals(10 * 1024, size1.getBytes());
    
    ByteSize size2 = new ByteSize("1.5MB");
    Assert.assertEquals((long) (1.5 * 1024 * 1024), size2.getBytes());
    
    ByteSize size3 = new ByteSize("2GB");
    Assert.assertEquals(2L * 1024 * 1024 * 1024, size3.getBytes());
  }

  @Test
  public void testTimeDurationParsing() {
    TimeDuration time1 = new TimeDuration("150ms");
    Assert.assertEquals(150 * 1_000_000, time1.getNanos());
    
    TimeDuration time2 = new TimeDuration("2.1s");
    Assert.assertEquals((long) (2.1 * 1_000_000_000), time2.getNanos());
    
    TimeDuration time3 = new TimeDuration("1m");
    Assert.assertEquals(60L * 1_000_000_000, time3.getNanos());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidByteSize() {
    new ByteSize("10XB");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTimeDuration() {
    new TimeDuration("10x");
  }
}