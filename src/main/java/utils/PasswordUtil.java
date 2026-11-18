package utils;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * 비밀번호를 평문 대신 PBKDF2 해시로 저장/검증 (JDK 기본 기능만 사용, 추가 라이브러리 없음).
 * 저장 형식: pbkdf2$반복횟수$솔트(Base64)$해시(Base64)  — 약 80자 (userpw 컬럼은 100자 이상 필요)
 */
public final class PasswordUtil {
    private static final String PREFIX = "pbkdf2";
    private static final int ITERATIONS = 120_000;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {}

    public static String hash(String raw) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        byte[] h = pbkdf2(raw, salt, ITERATIONS);
        Base64.Encoder b64 = Base64.getEncoder();
        return PREFIX + "$" + ITERATIONS + "$" + b64.encodeToString(salt) + "$" + b64.encodeToString(h);
    }

    public static boolean matches(String raw, String stored) {
        if (raw == null || stored == null) return false;
        String[] p = stored.split("\\$");
        if (p.length != 4 || !PREFIX.equals(p[0])) return false;
        byte[] salt = Base64.getDecoder().decode(p[2]);
        byte[] expected = Base64.getDecoder().decode(p[3]);
        byte[] actual = pbkdf2(raw, salt, Integer.parseInt(p[1]));
        return MessageDigest.isEqual(expected, actual); // 타이밍 공격 방지 비교
    }

    /** 예전 방식(평문)으로 저장된 값인지 */
    public static boolean isLegacyPlain(String stored) {
        return stored != null && !stored.startsWith(PREFIX + "$");
    }

    private static byte[] pbkdf2(String raw, byte[] salt, int iterations) {
        try {
            PBEKeySpec spec = new PBEKeySpec(raw.toCharArray(), salt, iterations, 256);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new IllegalStateException("비밀번호 해시 실패", e);
        }
    }
}
