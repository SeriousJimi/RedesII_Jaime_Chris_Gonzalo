package client;

/*
 * This class helps generate HTTP requests in string format,
 * wrapping around the Request class for easier use.
 */
public class RequestCreator {

    public RequestCreator() {
    }

    public String makeRequest(String host, String verb, String[] headers, String body) {
        try {
            String headerString = "";
            for (int i = 0; i < headers.length; i++) {
                headerString = headerString + headers[i];
            }
            Request request = new Request(host, verb,headers.length ,headerString, body);
            return request.toString();
        } catch (Exception e) {
            ErrorHandler.handle(e, "Failed to create HTTP request.");
            return null;
        }
    }
}
