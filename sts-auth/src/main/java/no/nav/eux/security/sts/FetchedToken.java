package no.nav.eux.security.sts;

public class FetchedToken {

    private String token;
    private long timeout;

    public FetchedToken(String token, long timeout) {
        this.token = token;
        this.timeout = timeout;
    }

    public String getToken() {
        return token;
    }

    public long getTimeout() {
        return timeout;
    }
}
