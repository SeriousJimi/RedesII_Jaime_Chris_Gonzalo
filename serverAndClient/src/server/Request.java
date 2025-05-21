package server;

public class Request {
    public String verb;
    public String path;
    public String[] headers;
    public String body;

    public Request(String rawRequest) {
        String[] lines = rawRequest.split("\n");

        verb = lines.length > 0 ? lines[0].trim() : "";
        path = lines.length > 1 ? lines[1].trim() : "";

        int headerCount = 0;
        if (lines.length > 2) {
            try {
                headerCount = Integer.parseInt(lines[2].trim());
            } catch (NumberFormatException e) {
                headerCount = 0;
            }
        }

        headers = new String[headerCount];
        for (int i = 0; i < headerCount && 3 + i < lines.length; i++) {
            headers[i] = lines[3 + i].trim();
        }

        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 3 + headerCount; i < lines.length; i++) {
            bodyBuilder.append(lines[i].trim());
        }

        body = bodyBuilder.toString();
    }

    public String getVerb() {
        return verb;
    }

    public String getPath() {
        return path;
    }

    public String[] getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public String[] getBodyParts() {
        return body.split(";");
    }
}
