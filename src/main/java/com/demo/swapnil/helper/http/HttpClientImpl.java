package com.demo.swapnil.helper.http;

import okhttp3.*;

import java.io.IOException;

public class HttpClientImpl implements HttpClient {

  public String post(final String url, final String contentType, final String body) throws IOException {
    final MediaType mediaType = MediaType.parse(contentType);
    return buildPostRequest(url, mediaType, body)
        .execute()
        .body()
        .string();
  }

  private Call buildPostRequest(final String url, final MediaType mediaType, final String content) {
    OkHttpClient okHttpClient = new OkHttpClient();
    return okHttpClient
        .newCall(
            new Request
                .Builder()
                .post(RequestBody.create(mediaType, content))
                .url(url)
                .build());
  }
}
