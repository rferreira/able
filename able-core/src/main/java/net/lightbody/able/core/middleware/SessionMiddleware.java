package net.lightbody.able.core.middleware;

import com.google.common.base.Charsets;
import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.lightbody.able.core.http.Request;
import net.lightbody.able.core.http.Response;
import net.lightbody.able.core.util.Log;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.net.idn.StringPrep;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * Session store backed by a signed cookie
 */
public class SessionMiddleware extends Middleware {

    public static final String SESSION_COOKIE_NAME = "session_id";
    public static final int SESSION_DEFAULT_AGE = 1209600;     // two weeks

    private static Log LOG = new Log();

    private BASE64Decoder decoder = new BASE64Decoder();
    private BASE64Encoder encoder = new BASE64Encoder();

    private SecretKeySpec key;

    // where we store the inflight request:
    private Request inflightRequest;

    public SessionMiddleware(@Named("secret") String secret) {
        key = new SecretKeySpec(secret.getBytes(), "HmacSHA1");
        LOG.fine("session logic started...");

    }

    @Override
    public void process(Request req) {
        // loading from cookie
        if (!req.COOKIES.containsKey(SESSION_COOKIE_NAME)) {
            return;
        }


        // reassembling the cookie
        try {
            String[] args = req.COOKIES.get(SESSION_COOKIE_NAME).split(":");

            String payload = new String(decoder.decodeBuffer(args[0]), Charsets.UTF_8);
            String signature = args[1];

            String calculatedSignature = sign(payload.getBytes(Charsets.UTF_8));

            if (!calculatedSignature.equals(signature)) {
                throw new InvalidSessionSignatureException();
            }

            Map sessionData = new Gson().fromJson(payload, new TypeToken<HashMap<String, Object>>() {
            }.getType());

            req.SESSION.putAll(sessionData);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // saving it for the response handling
        inflightRequest = req;


    }

    @Override
    public void process(Response resp) {
        // save cookie - we should make this selective in the future so only dirty sessions are flushed
        String js = new Gson().toJson(inflightRequest.SESSION);
        String b64 = encoder.encode(js.getBytes(Charsets.UTF_8));
        String signature = sign(js.getBytes(Charsets.UTF_8));

        resp.setCookie(SESSION_COOKIE_NAME, b64 + ":" + signature, SESSION_DEFAULT_AGE, "/", inflightRequest.HEADERS.get(HttpHeaders.HOST), false, true);
    }

    private String sign(byte[] data) {
        try {
            Mac m = Mac.getInstance(key.getAlgorithm());
            m.init(key);
            byte[] signedData = m.doFinal(data);
            return encoder.encode(signedData);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public class InvalidSessionSignatureException extends RuntimeException {
        public InvalidSessionSignatureException() {
            super("your session data in potentially being tempered with, signature sent is not correct");
        }
    }

}
