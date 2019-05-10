package oppslag.sts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RestStsClientIT {
  private static final String URL = "url";
  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";

  private RestStsClient restStsClient;

  @BeforeEach
  public void configureTrustStore() {
    String url = System.getProperty(URL);
    String username = System.getProperty(USERNAME);
    String password = System.getProperty(PASSWORD);
    restStsClient = new RestStsClient(url, username, password);
  }

  @Test
  public void getAccessTokenFromSts() {
    assertNotNull(restStsClient.getAccessTokenFromSts());
    assertThat(restStsClient.getAccessTokenFromSts().length()).isGreaterThan(0);
  }
}
