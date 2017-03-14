package personal.shuaiz.utils;

import com.sun.jna.Platform;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

public class NamedMutexTest {
  @Test
  public void testWindows() throws Exception {
    Assume.assumeTrue(Platform.isWindows());

    try (NamedMutex mutex = NamedMutex.newInstance("test")) {
      Assert.assertTrue(mutex instanceof NamedMutexWindowsImpl);
    }
  }
}
