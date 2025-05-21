package client;

public class Snake {
    private String name;
    private String size;
    private String color;

    /**
     * Constructor that parses a bodyPart string formatted as "name,size,color"
     */
    public Snake(String bodyPart) {
        try {
            String[] bodyParts = bodyPart.split(",");
            name = bodyParts[0];
            size = bodyParts[1];
            color = bodyParts[2];
        } catch (Exception e) {
            ErrorHandler.handle(e, "Failed to parse snake data.");
            // Optional: set default values to avoid nulls
            name = "placeholder";
            size = "placeholder";
            color = "placeholder";
        }
    }

    public Snake(String name, String size, String color) {
        this.name = name;
        this.size = size;
        this.color = color;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return name + "," + size + "," + color;
    }
}
