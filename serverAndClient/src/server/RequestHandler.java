package server;

import client.Snake;

public class RequestHandler {

    private final SnakeDatabase db;
    private final Request request;
    private String response;

    public RequestHandler(String rawRequest, SnakeDatabase db) {
        this.db = db;
        this.request = new Request(rawRequest);
        analyzeVerb();
    }

    private void analyzeVerb() {
        switch (request.getVerb().toLowerCase()) {
            case "get":
                handleGetRequest();
                break;
            case "head":
                handleHeadRequest();
                break;
            case "post":
                handlePostRequest();
                break;
            case "put":
                handlePutRequest();
                break;
            case "delete":
                handleDeleteRequest();
                break;
        }
    }

    private void handleGetRequest() {
        String body = request.getBody();
        StringBuilder builder = new StringBuilder();

        if (body.isEmpty()) {
            for (Snake s : db.getAll()) {
                builder.append(s.toString()).append("\n");
            }
        } else {
            for (String part : request.getBodyParts()) {
                String[] attr = part.split(",");
                if (attr.length == 2 && attr[0].equals("Attribute")) {
                    String value = attr[1];
                    for (Snake s : db.filterByAttribute(value)) {
                        builder.append(s.toString()).append("\n");
                    }
                }
            }
        }

        response = builder.toString();
    }

    private void handleHeadRequest() {
    String body = request.getBody();
    StringBuilder builder = new StringBuilder();

    if (body.isEmpty()) {
        for (String[] head : db.getAllHeads()) {
            builder.append("[");
            for (int i = 0; i < head.length; i++) {
                builder.append(head[i]);
                if (i < head.length - 1) builder.append(", ");
            }
            builder.append("]\n");
        }
    } else {
        boolean found = false;
        for (String part : request.getBodyParts()) {
            String[] attr = part.split(",");
            if (attr.length == 2 && attr[0].equals("Attribute")) {
                for (String[] h : db.filterHeadByAttribute(attr[1])) {
                    found = true;
                    builder.append("[");
                    for (int i = 0; i < h.length; i++) {
                        builder.append(h[i]);
                        if (i < h.length - 1) builder.append(", ");
                    }
                    builder.append("]\n");
                }
            }
        }

        if (!found) {
            builder.append("No headers found for given attribute.");
        }
    }

    response = builder.toString();
}


    private void handlePostRequest() {
        for (String part : request.getBodyParts()) {
            String[] attr = part.split(",");
            if (attr.length == 4 && attr[0].equals("Snake")) {
                Snake newSnake = new Snake(attr[1], attr[2], attr[3]);
                if (db.add(newSnake, request.headers)) {
                    response = "Snake added";
                } else {
                    response = "Snake already exists";
                }
            }
        }
    }

    private void handlePutRequest() {
        for (String part : request.getBodyParts()) {
            String[] attr = part.split(",");
            if (attr.length == 4 && attr[0].equals("Snake")) {
                Snake updated = new Snake(attr[1], attr[2], attr[3]);
                if (db.update(updated)) {
                    response = "Snake " + attr[1] + " modified";
                } else {
                    response = "Snake " + attr[1] + " could not be modified";
                }
            }
        }
    }

    private void handleDeleteRequest() {
        String[] bodyParts = request.getBodyParts();
        if (bodyParts.length > 0) {
            String[] attr = bodyParts[0].split(",");
            if (attr.length == 2 && attr[0].equals("Attribute")) {
                String name = attr[1];
                if (db.deleteByName(name)) {
                    response = "Snake " + attr[1] + " deleted";
                } else {
                    response = "Snake " + attr[1] + " could not be deleted";
                }
            }
        }
    }

    public String returnResponse() {
        return response;
    }
}
