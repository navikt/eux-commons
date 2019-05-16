package oppslag.sts;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class BearerTokenClientRequestInterceptor implements ClientHttpRequestInterceptor {

  private final RestStsClient restStsClient;

  @Autowired
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
