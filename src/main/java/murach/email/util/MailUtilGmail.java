package murach.email.util;

import jakarta.mail.MessagingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class MailUtilGmail {

    // Webhook URL của Google Apps Script chạy qua cổng 443 HTTPS (Không bị Render chặn)
    private static final String WEBHOOK_URL = System.getenv("MAIL_WEBHOOK_URL") != null
            ? System.getenv("MAIL_WEBHOOK_URL")
            : "https://script.google.com/macros/s/AKfycbwBGh_6dRvBKn-v9nfcRQuhjJJv4DXFaIWOLSFrc3fpB-zk_3Ox0x_d3UuJqC7z_br17Q/exec";

    public static void sendMail(String to, String from,
            String subject, String body, boolean bodyIsHTML)
            throws MessagingException {

        try {
            // Chuẩn bị payload JSON
            String escapedSubject = escapeJson(subject);
            String escapedBody = escapeJson(body);

            String jsonPayload = "{"
                    + "\"to\":\"" + to + "\","
                    + "\"subject\":\"" + escapedSubject + "\","
                    + "\"body\":\"" + escapedBody + "\","
                    + "\"isHTML\":" + bodyIsHTML
                    + "}";

            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NEVER)
                    .connectTimeout(Duration.ofSeconds(15))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(WEBHOOK_URL))
                    .timeout(Duration.ofSeconds(20))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Google Apps Script trả về 302 chuyển hướng đến trang kết quả JSON
            if (response.statusCode() == 302) {
                String location = response.headers().firstValue("location").orElse(null);
                if (location != null) {
                    HttpRequest getReq = HttpRequest.newBuilder()
                            .uri(URI.create(location))
                            .timeout(Duration.ofSeconds(20))
                            .GET()
                            .build();
                    response = client.send(getReq, HttpResponse.BodyHandlers.ofString());
                }
            }

            if (response.statusCode() != 200 || !response.body().contains("\"status\":\"success\"")) {
                throw new MessagingException("Google Apps Script Error (Status " + response.statusCode() + "): " + response.body());
            }

        } catch (MessagingException me) {
            throw me;
        } catch (Exception e) {
            throw new MessagingException("Failed to send email via Google Apps Script (port 443): " + e.getMessage(), e);
        }
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
