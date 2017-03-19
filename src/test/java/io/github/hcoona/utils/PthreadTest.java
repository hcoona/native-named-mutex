package io.github.hcoona.utils;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

public class PthreadTest {

  @BeforeClass
  public static void beforeClass() throws Exception {
    Assume.assumeTrue(Platform.isLinux());
  }

  @Test
  public void testSelfPid() throws Exception {
    Assert.assertNotEquals(0, Pthread.INSTANCE.pthread_self());
  }

  @Test
  public void testSemaphore() throws Exception {
    final String name = "/test";

    Pointer sem = Pthread.INSTANCE.sem_open(name, Pthread.Oflag.O_CREAT,
        Pthread.Mode.S_IRUSR | Pthread.Mode.S_IWUSR, 0);
    if (sem == null) {
      Assert.fail(LibCExt.INSTANCE.strerror(Native.getLastError()));
    } else {
      //Assert.assertEquals(0, Pthread.INSTANCE.sem_post(sem));
      Assert.assertEquals(0, Pthread.INSTANCE.sem_close(sem));
      Assert.assertEquals(0, Pthread.INSTANCE.sem_unlink(name));
    }
  }

}
