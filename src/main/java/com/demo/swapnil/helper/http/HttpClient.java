package com.demo.swapnil.helper.http;

import java.io.IOException;

public interface HttpClient {
  String post(final String url, final String contentType, final String body) throws IOException;
}
