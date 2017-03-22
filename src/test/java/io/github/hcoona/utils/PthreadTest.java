package io.github.hcoona.utils;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
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

  @Test(timeout=5000)
  public void testSemaphore() throws Exception {
    final String name = "/test";

    Pointer sem = Pthread.INSTANCE.sem_open(name, Pthread.Oflag.O_CREAT,
        Pthread.Mode.S_IRWXU | Pthread.Mode.S_IRUSR | Pthread.Mode.S_IWUSR,
        0);
    if (sem == null) {
      Assert.fail(LibCExt.INSTANCE.strerror(Native.getLastError()));
    } else {
      Assert.assertEquals(-1, Pthread.INSTANCE.sem_trywait(sem));
      Assert.assertEquals(Errno.EAGAIN, Native.getLastError());
      Assert.assertEquals(0, Pthread.INSTANCE.sem_post(sem));
      IntByReference value = new IntByReference(0);
      Assert.assertEquals(0, Pthread.INSTANCE.sem_getvalue(sem, value));
      Assert.assertEquals(1, value.getValue());
      Assert.assertEquals(0, Pthread.INSTANCE.sem_trywait(sem));
      Assert.assertEquals(-1, Pthread.INSTANCE.sem_trywait(sem));
      Assert.assertEquals(Errno.EAGAIN, Native.getLastError());
      Pthread.TimeSpecByReference timeout = new Pthread.TimeSpecByReference(1, 0);
      Assert.assertEquals(-1, Pthread.INSTANCE.sem_timedwait(sem, timeout));
      Assert.assertEquals(Errno.ETIMEDOUT, Native.getLastError());
      Assert.assertEquals(0, Pthread.INSTANCE.sem_close(sem));
      Assert.assertEquals(0, Pthread.INSTANCE.sem_unlink(name));
    }
  }

}
