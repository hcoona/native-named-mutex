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
    int O_CREAT = 0_100;
    int O_EXCL = 0_200;
  }

  ;

  interface Mode {
    int S_IRWXU = 0_0700;
    int S_IRUSR = 0_0400;
    int S_IWUSR = 0_0200;
    int S_IXUSR = 0_0100;
    int S_IRWXG = 0_0070;
    int S_IRGRP = 0_0040;
  }

  Pointer sem_open(String name, int oflag);

  Pointer sem_open(String name, int oflag, int mode, int value);

  int sem_wait(Pointer sem);

  int sem_trywait(Pointer sem);

  class TimeSpec extends Structure {
    public long tv_sec;
    public long tv_nsec;

    public TimeSpec(long seconds, long nanoseconds) {
      this.tv_sec = seconds;
      this.tv_nsec = nanoseconds;
    }

    @Override
    protected List<String> getFieldOrder() {
      return Arrays.asList("tv_sec", "tv_nsec");
    }
  }

  class TimeSpecByReference extends TimeSpec implements Structure.ByReference {
    public TimeSpecByReference(long seconds, long nanoseconds) {
      super(seconds, nanoseconds);
    }
  }

  int sem_timedwait(Pointer sem, TimeSpecByReference abs_timeout);

  int sem_post(Pointer sem);

  int sem_getvalue(Pointer sem, IntByReference sval);

  int sem_destroy(Pointer sem);

  int sem_close(Pointer sem);

  int sem_unlink(String name);
}