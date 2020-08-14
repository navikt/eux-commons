package no.nav.eux.security.sts;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class ExtendedBearerTokenClientRequestInterceptor extends BearerTokenClientRequestInterceptor{
    public ExtendedBearerTokenClientRequestInterceptor(RestStsClient restStsClient) {
        super(restStsClient);
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        final String token = restStsClient.getAccessTokenFromSts();
        request.getHeaders().add(
                "Nav-Consumer-Token", "Bearer " + token);

        return super.intercept(request, body, execution);
    }
}
