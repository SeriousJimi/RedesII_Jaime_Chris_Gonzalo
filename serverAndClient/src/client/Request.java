package client;


public class Request {
    String host;
    String verb;
    int numberOfHeaders = 0;
    String[] headers;
    String body;
    String[] bodyParts;
    /*
     * Here we take a string and build it into a resource type object, this class is then used to know what response to make
     */
    public Request(String host, String verb, int numberOfHeaders,String headers, String body) {
        this.host = host;
        this.verb = verb;
        this.numberOfHeaders = numberOfHeaders;
        this.headers = headers.split("\n");
        this.body = body;
        separateBody();
    }

    public Request(String request){
        String[] lines = request.split("\n");
        host = lines[0];
        verb = lines[1];
        numberOfHeaders = Integer.parseInt(lines[2]);
        headers = new String[numberOfHeaders];
        

        for (int i = 0; i < numberOfHeaders; i++) {
            headers[i] = lines[3 + i];
            
        }
        
        body = lines[ 3 + numberOfHeaders];
        separateBody();
    }
    private void separateBody(){
        bodyParts = body.split(";");
    }
    /*
     * body parts are separated with a ;, this way we can have the body have different types of arguments
     *  
     */
    public void addBodyPart(String bodyPart){
        body = body + bodyPart;
        separateBody();
    }
    public void addHeader(String header){
        numberOfHeaders++;
        String[] newHeaders = new String[numberOfHeaders];
        for (int i = 0; i < headers.length - 1; i++) {
            newHeaders[i] = headers[i];
        }
        newHeaders[numberOfHeaders - 1] = header;
    }
    public String getVerb(){
        return verb;
    }
    public String getBody(){
        return body;
    }
    public String[] getBodyParts(){
        return bodyParts;
    }
    /*
     * Put a toString here
     */
     @Override
    public String toString() {
        return host + "\n" + verb + "\n" + numberOfHeaders + "\n" + headersToString()  + body;
    }
    private String headersToString(){
        String header = "";
        for (int i = 0; i < numberOfHeaders; i++){
            header = header + headers[i] + "\n";
        }
        return header;
        
    }
}
