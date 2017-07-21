package uk.co.tezk.mymarvelapplication.utility;

import java.security.MessageDigest;

/**
 * Created by Terry Kay
 *
 * function to generate MD5 hash
 */

public class MD5 {
    public static String getMd5Hash(String string) {
        /**
         * Generate MD5 hash of input string
         * @param string the String to hash
         * @return the MD5 hash
         */

        byte[] thedigest = null;
        byte[] bytesOfMessage = new byte[0];
        try {
            bytesOfMessage = string.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            thedigest = md.digest(bytesOfMessage);
        } catch (Exception e) {
            throw new RuntimeException("Error computing hash : "+e.getMessage());
        }

        StringBuilder sb = new StringBuilder(2 * thedigest.length);
        for (byte b : thedigest) {
            sb.append("0123456789abcdef".charAt((b & 0xF0) >> 4));
            sb.append("0123456789abcdef".charAt((b & 0x0F)));
        }

        return sb.toString();
    }
}
