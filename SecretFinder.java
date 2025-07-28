import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import org.json.*;

public class SecretFinder {

    static class Share {
        long x;
        BigInteger y;
        Share(long x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }

    public static List<Share> parseJson(String filePath) throws IOException {
        List<Share> shares = new ArrayList<>();
        String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(content);

        for (String key : json.keySet()) {
            if (key.equals("keys")) continue;
            long x = Long.parseLong(key);
            JSONObject obj = json.getJSONObject(key);
            int base = Integer.parseInt(obj.getString("base"));
            String value = obj.getString("value");
            BigInteger y = new BigInteger(value, base);
            shares.add(new Share(x, y));
        }
        return shares;
    }

    public static BigInteger lagrangeInterpolation(List<Share> shares) {
        BigInteger secret = BigInteger.ZERO;

        for (int i = 0; i < shares.size(); i++) {
            BigInteger xi = BigInteger.valueOf(shares.get(i).x);
            BigInteger yi = shares.get(i).y;
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (int j = 0; j < shares.size(); j++) {
                if (i == j) continue;
                BigInteger xj = BigInteger.valueOf(shares.get(j).x);
                numerator = numerator.multiply(xj.negate());
                denominator = denominator.multiply(xi.subtract(xj));
            }

            BigInteger term = yi.multiply(numerator).divide(denominator);
            secret = secret.add(term);
        }

        return secret;
    }

    public static List<List<Share>> generateCombinations(List<Share> shares, int k) {
        List<List<Share>> result = new ArrayList<>();
        backtrack(result, new ArrayList<>(), shares, 0, k);
        return result;
    }

    public static void backtrack(List<List<Share>> result, List<Share> temp, List<Share> shares, int start, int k) {
        if (temp.size() == k) {
            result.add(new ArrayList<>(temp));
            return;
        }
        for (int i = start; i < shares.size(); i++) {
            temp.add(shares.get(i));
            backtrack(result, temp, shares, i + 1, k);
            temp.remove(temp.size() - 1);
        }
    }

    public static void main(String[] args) throws IOException {
        String filePath = "src/testcase2.json"; // Change path if needed
        List<Share> allShares = parseJson(filePath);
        JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8));
        int k = json.getJSONObject("keys").getInt("k");

        Map<BigInteger, Integer> secretCount = new HashMap<>();
        List<List<Share>> allCombos = generateCombinations(allShares, k);

        for (List<Share> combo : allCombos) {
            try {
                BigInteger secret = lagrangeInterpolation(combo);
                secretCount.put(secret, secretCount.getOrDefault(secret, 0) + 1);
            } catch (ArithmeticException e) {
                // Skip bad combination if any division fails
            }
        }

        BigInteger finalSecret = null;
        int maxCount = 0;
        for (Map.Entry<BigInteger, Integer> entry : secretCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                finalSecret = entry.getKey();
            }
        }

        System.out.println("Secret: " + finalSecret + " (appeared " + maxCount + " times)");
    }
}
