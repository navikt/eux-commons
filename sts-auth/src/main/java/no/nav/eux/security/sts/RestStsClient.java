package no.nav.eux.security.sts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class RestStsClient {

  private static final long REFRESH_TIMEOUT_MARGIN_MS = 300_000L;
  private static final Logger log = LoggerFactory.getLogger(RestStsClient.class);

  private final String username;
  private final String password;
  private final String completeUrl;
  private final AtomicReference<FetchedToken> token = new AtomicReference<>();
  private RestTemplate restTemplate;

  public RestStsClient(String baseUrl,
                       String username,
                       String password) {
    this.username = username;
    this.password = password;
    this.completeUrl = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/v1/sts/token")
            .queryParam("grant_type", "client_credentials")
            .queryParam("scope", "openid")
            .toUriString();
  }

  public String getAccessTokenFromSts() {


    if (token.get() == null || token.get().getTimeout() < System.currentTimeMillis() ) {
      if (restTemplate == null) {
        restTemplate = getRestTemplate();
      }
      log.debug("getting access_token from security-token-service");

      final Map response = restTemplate.getForObject(completeUrl, Map.class);
      if (response != null) {
        String accessToken = (String) response.get("access_token");
        String expiresIn = (String) response.get("expires_in");
        long expirationTime;
        try {
          expirationTime = System.currentTimeMillis() + Long.parseLong(expiresIn) * 1000L - REFRESH_TIMEOUT_MARGIN_MS;
        } catch (RuntimeException e) {
          expirationTime = System.currentTimeMillis();
        }
        FetchedToken fetchedToken = new FetchedToken(accessToken, expirationTime);
        token.set(fetchedToken);
      } else {
        token.set(null);
      }

    }
    return token.get() == null ? null : token.get().getToken();
  }

  private RestTemplate getRestTemplate() {
    //As RestTemplate is not threadsafe, creating a local instance.
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setInterceptors(getInteceptorsList());
    return restTemplate;
  }

  private List getInteceptorsList() {
    List listInterceptor = new ArrayList();
    listInterceptor.add(new AddHeadersInterceptor());
    listInterceptor.add(new BasicAuthenticationInterceptor(username, password));
    return listInterceptor;
  }
}
