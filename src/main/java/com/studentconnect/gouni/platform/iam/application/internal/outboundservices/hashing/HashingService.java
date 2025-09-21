package com.studentconnect.gouni.platform.iam.application.internal.outboundservices.hashing;

public interface HashingService {
  String encode(CharSequence rawPassword);

  boolean matches(CharSequence rawPassword, String encodedPassword);
}
