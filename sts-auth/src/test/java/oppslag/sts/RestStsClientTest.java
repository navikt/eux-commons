package oppslag.sts;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class RestStsClientTest {

  private RestStsClient restStsClient;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    restStsClient = Mockito.spy(new RestStsClient("", "", ""));
    doReturn("mytoken").when(restStsClient).getAccessTokenFromSts();
  }

  @Test
  public void getAccessTokenFromSts() {
    assertThat(restStsClient.getAccessTokenFromSts()).isEqualTo("mytoken");
  }
}