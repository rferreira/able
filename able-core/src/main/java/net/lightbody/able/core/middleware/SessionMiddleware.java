package net.lightbody.able.core.middleware;

import com.google.common.base.Charsets;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.lightbody.able.core.http.Request;
import net.lightbody.able.core.http.Response;
import net.lightbody.able.core.util.Log;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Session store backed by a signed cookie
 * TODO: figure out a way to generate keys in the config file
 * TODO: expire at browser closing support
 */
public class SessionMiddleware extends Middleware {

    public static final String SESSION_COOKIE_NAME = "sessionid";
    public static final int SESSION_DEFAULT_AGE = 1209600;     // two weeks

    private static Log LOG = new Log();

    private boolean compressionEnabled = false;

    private SecretKeySpec key;

    // where we store the inflight request:
    private Request inflightRequest;
    private String original;


    @Inject
    public SessionMiddleware(@Named("session.secret") String secret, @Named("session.compression") boolean compress) {
        key = new SecretKeySpec(secret.getBytes(Charsets.UTF_8), "HmacSHA1");
        this.compressionEnabled = compress;

    }

    @Override
    public void process(Request req) {

        // saving it for the response handling
        inflightRequest = req;

        // loading from cookie
        if (!req.COOKIES.containsKey(SESSION_COOKIE_NAME)) {
            return;
        }

        // reassembling the cookie
        try {
            String[] args = req.COOKIES.get(SESSION_COOKIE_NAME).split(":");

            String payload = args[0];
            String signature = args[1];

            // saving original state
            original = payload;

            String calculatedSignature = sign(payload.getBytes(Charsets.UTF_8));

            if (!calculatedSignature.equals(signature)) {
                throw new InvalidSessionSignatureException();
            }

            Map sessionData = (Map) safeDecode(payload);

            req.SESSION.putAll(sessionData);

        } catch (Exception e) {
            LOG.info("invalid session data from cookie - ignoring", e);
            original = null; // forcing a reload
        }
    }

    @Override
    public void process(Response resp) {

        try {
            String data = safeEncode(inflightRequest.SESSION);

            if (data.equals(original)) {
                LOG.fine("session data not modified, skipping....");
                return;
            }

            String signature = sign(data.getBytes());

            LOG.fine("saving modified session data");
            resp.setCookie(SESSION_COOKIE_NAME, data + ":" + signature, SESSION_DEFAULT_AGE, "/", null, false, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String sign(byte[] data) {
        try {
            Mac m = Mac.getInstance(key.getAlgorithm());
            m.init(key);
            byte[] signedData = m.doFinal(data);
            return Base64.encodeBase64URLSafeString(signedData);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public boolean isCompressionEnabled() {
        return compressionEnabled;
    }

    public void setCompressionEnabled(boolean compressionEnabled) {
        this.compressionEnabled = compressionEnabled;
    }

    public class InvalidSessionSignatureException extends RuntimeException {
        public InvalidSessionSignatureException() {
            super("your session data in potentially being tempered with, signature sent is not correct");
        }
    }

    public String safeEncode(Object o) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        ObjectOutput out;

        if (compressionEnabled) {
            out = new ObjectOutputStream(new GZIPOutputStream(buffer));
        } else {
            out = new ObjectOutputStream(buffer);

        }
        out.writeObject(o);
        out.close();
        return Base64.encodeBase64URLSafeString(buffer.toByteArray());
    }

    public Object safeDecode(String s) throws IOException, ClassNotFoundException {

        ByteArrayInputStream buffer = new ByteArrayInputStream(Base64.decodeBase64(s));

        ObjectInputStream in;

        if (compressionEnabled) {
            in = new ObjectInputStream(new GZIPInputStream(buffer));
        } else {
            in = new ObjectInputStream(buffer);
        }

        Object o = in.readObject();
        in.close();
        return o;
    }

}
