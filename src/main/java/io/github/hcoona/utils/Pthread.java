package io.github.hcoona.utils;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;

import java.util.Arrays;
import java.util.List;

public interface Pthread extends Library {
  Pthread INSTANCE = Native.loadLibrary("pthread", Pthread.class);

  int pthread_self();

  interface Oflag {
    int O_CREAT = 0x100;
    int O_EXCL = 0x200;
  };

  interface Mode {
    int S_IRWXU = 00700;
    int S_IRUSR = 00400;
    int S_IWUSR = 00200;
    int S_IXUSR = 00100;
    int S_IRWXG = 00070;
    int S_IRGRP = 00040;
  }

  Pointer sem_open(String name, int oflag);

  Pointer sem_open(String name, int oflag, int mode, int value);

  int sem_wait(Pointer sem);

  int sem_trywait(Pointer sem);

  class TimeSpec extends Structure {
    public static class ByReference extends TimeSpec implements Structure.ByReference {
      public ByReference(long seconds, long nanoseconds) {
        super(seconds, nanoseconds);
      }
    }

    public long tv_sec;
    public long tv_nsec;

    public TimeSpec( long seconds, long nanoseconds ) {
      this.tv_sec = seconds;
      this.tv_nsec = nanoseconds;
    }

    @Override
    protected List<String> getFieldOrder() {
      return Arrays.asList("tv_sec", "tv_nsec");
    }
  }

  int sem_timedwait(Pointer sem, TimeSpec.ByReference abs_timeout);

  int sem_post(Pointer sem);

  int sem_getvalue(Pointer sem, IntByReference sval);

  int sem_destroy(Pointer sem);

  int sem_close(Pointer sem);

  int sem_unlink(String name);
}