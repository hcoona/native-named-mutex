package io.github.hcoona.utils;

import com.sun.jna.LastErrorException;

public class PosixErrorException extends LastErrorException {
  public PosixErrorException(int code) {
    super(code, LibCExt.INSTANCE.strerror(code));
  }
}
