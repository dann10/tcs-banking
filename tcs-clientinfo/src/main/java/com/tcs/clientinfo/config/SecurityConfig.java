package com.tcs.clientinfo.config;

import org.mindrot.jbcrypt.BCrypt;

public class SecurityConfig {
  public static String hashPassword(String plainPassword) {
    return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
  }
}
