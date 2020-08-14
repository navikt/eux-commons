package no.nav.eux.security.sts;

import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class BearerTokenClientRequestInterceptor implements ClientHttpRequestInterceptor {

  protected final RestStsClient restStsClient;

  public BearerTokenClientRequestInterceptor(RestStsClient restStsClient) {
    this.restStsClient = restStsClient;
  }

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
      ClientHttpRequestExecution execution) throws IOException {
    final String token = restStsClient.getAccessTokenFromSts();
    request.getHeaders().add(
        "Authorization", "Bearer " + token);
    return execution.execute(request, body);
  }
}
