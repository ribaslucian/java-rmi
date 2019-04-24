package GenericJavaRMI;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

public class Utils {

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static String[] remove(String[] data, String delete) {
        if (data.length == 1 && data[0].equals(delete)) {
            String[] r = {};
            return r;
        }

        String dataString = "";

        for (int i = 0; i < data.length; i++) {
            if (!data[i].equals(delete)) {
                dataString += data[i] + ",";
            }
        }

        dataString = dataString.substring(0, dataString.length() - 1);

        return dataString.split(",");
    }

    public static String implode(String[] data) {
        if (data.length == 0) {
            return "";
        }

        String dataString = "";

        for (int i = 0; i < data.length; i++) {
            dataString += data[i] + ",";
        }

//        if (data.length > 1)
        dataString = dataString.substring(0, dataString.length() - 1);

        return dataString;
    }

    public static PublicKey stringToPublicKey(String publicK) {
        try {
            byte[] publicBytes = Base64.getDecoder().decode(publicK);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    public static String toBase64(byte[] str) {
        return new String(Base64.getEncoder().encodeToString(str));
    }

    public static byte[] base64ToBytes(String str) {
        return Base64.getDecoder().decode(str);
    }

}
