package oppslag.sts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class RestStsClient {

  private static final Logger log = LoggerFactory.getLogger(RestStsClient.class);
  private String baseUrl;
  private String username;
  private String password;

  public RestStsClient(String baseUrl,
                       String username,
                       String password) {
    this.baseUrl = baseUrl;
    this.username = username;
    this.password = password;
  }

  public String getAccessTokenFromSts() {
    final String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
        .path("/v1/sts/token")
        .queryParam("grant_type", "client_credentials")
        .queryParam("scope", "openid")
        .toUriString();
    log.debug("getting access_token from security-token-service");

    //As RestTemplate is not threadsafe, creating a local instance.
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setInterceptors(getInteceptorsList());

    final Map response = restTemplate.getForObject(url, Map.class);
    return response != null ? (String) response.get("access_token") : null;
  }

  private List getInteceptorsList() {
    List listInterceptor = new ArrayList();
    listInterceptor.add(new AddHeadersInterceptor());
    listInterceptor.add(new BasicAuthenticationInterceptor(username, password));
    return listInterceptor;
  }
}
