package com.demo.swapnil.helper.configuration;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Getter
public class ConfigurationImpl implements Configuration {
  private Properties props;

  public ConfigurationImpl() throws IOException {
    final InputStream resourceAsStream = ConfigurationImpl.class.getClassLoader().getResourceAsStream("app" +
        ".properties");
    props = new Properties();
    props.load(resourceAsStream);
  }

  public String getProp(String key) {
    return props.getProperty(key);
  }
}

