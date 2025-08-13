package finalProj.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class EcpayUtils {

	
	public static String generateCheckMacValue(Map<String, String> params, String hashKey, String hashIV) throws Exception {
	    Map<String, String> filtered = params.entrySet().stream()
	        .filter(e -> e.getValue() != null && !e.getValue().isEmpty())
	        .filter(e -> !e.getKey().equalsIgnoreCase("CheckMacValue"))
	        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

	    Map<String, String> sorted = new TreeMap<>(filtered);

	    StringBuilder sb = new StringBuilder();
	    sb.append("HashKey=").append(hashKey);
	    for (Map.Entry<String, String> entry : sorted.entrySet()) {
	        sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
	    }
	    sb.append("&HashIV=").append(hashIV);

	    // Step 3: 印出原始字串
	    String raw = sb.toString();
	    System.out.println("👉 Step 3 原始字串：" + raw);

	    // Step 4: URL encode
	    String encoded = URLEncoder.encode(raw, "UTF-8").toLowerCase()
	        .replaceAll("%21", "!")
	        .replaceAll("%28", "(")
	        .replaceAll("%29", ")")
	        .replaceAll("%2a", "*")
	        .replaceAll("%2d", "-")
	        .replaceAll("%2e", ".")
	        .replaceAll("%5f", "_");

	    System.out.println("👉 Step 4 URL Encoded 字串：" + encoded);

	    // Step 5: SHA256
	    MessageDigest md = MessageDigest.getInstance("SHA-256");
	    byte[] digest = md.digest(encoded.getBytes(StandardCharsets.UTF_8));
	    StringBuilder hex = new StringBuilder();
	    for (byte b : digest) {
	        hex.append(String.format("%02X", b));
	    }

	    System.out.println("👉 Step 5 CheckMacValue 雜湊結果：" + hex.toString());

	    return hex.toString();
	}





}