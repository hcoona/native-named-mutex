package personal.shuaiz.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class NamedMutexWindowsImplTest {
  @Test
  public void testCannotWaitOne() throws Exception {
    final String name = "test_mutex1";
    try (NamedMutex mutex_owned = new NamedMutexWindowsImpl(true, name)) {
      ExecutorService executor = Executors.newSingleThreadExecutor();

      Future<Boolean> waitOneFuture = executor.submit(() -> {
        try (NamedMutex mutex2 = new NamedMutexWindowsImpl(true, name)) {
          return mutex2.waitOne(500, TimeUnit.MILLISECONDS);
        }
      });

      Assert.assertFalse(waitOneFuture.get());
    }
  }

  @Test
  public void testCanWaitOne() throws Exception {
    final String name = "test_mutex1";
    try (NamedMutex mutex_owned = new NamedMutexWindowsImpl(true, name)) {
      mutex_owned.release();
      ExecutorService executor = Executors.newSingleThreadExecutor();

      Future<Boolean> waitOneFuture = executor.submit(() -> {
        try (NamedMutex mutex2 = new NamedMutexWindowsImpl(true, name)) {
          return mutex2.waitOne(5, TimeUnit.SECONDS);
        }
      });

      Assert.assertTrue(waitOneFuture.get());
    }
  }
}
