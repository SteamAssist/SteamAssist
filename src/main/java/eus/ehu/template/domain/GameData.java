package eus.ehu.template.domain;

public class GameData {

    private String name;
    private String imageUrl;
    private String mainLength;
    private String mainSidesLength;
    private String allLength;
    private String completeLength;
    private String price;
    private String description;

    public GameData(String name, String imageUrl, String mainLength, String mainSidesLength, String completeLength, String allLength, String description, String price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.mainLength = mainLength;
        this.mainSidesLength = mainSidesLength;
        this.completeLength = completeLength;
        this.allLength = allLength;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getMainLength() {
        return mainLength;
    }
    public String getMainSidesLength() {
        return mainSidesLength;
    }
    public String getCompleteLength() {
        return completeLength;
    }
    public String getAllLength() {
        return allLength;
    }

    public String getDescription() {
        return description;
    }
    public String getPrice() {
        return price;
    }




}
