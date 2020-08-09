package commons;

import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.security.NoSuchAlgorithmException;

/**
 * @author JakeXsc
 * @version 1.0
 * @date 2020/8/10 0:00
 */
public class AclUtils {
    public static String getDigestUserPwd(String id) throws NoSuchAlgorithmException {
        return DigestAuthenticationProvider.generateDigest(id);
    }
}
