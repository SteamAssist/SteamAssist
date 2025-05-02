package eus.ehu.template.domain;

import com.google.gson.annotations.SerializedName;

public class GameData {
    private boolean success;
    private String gameId;
    private Data data;

    // Getters
    public Data getData() {
        return data;
    }

    public static class Data {
        private String title;
        private String description;
        private String price;
        private Times times;
        private Image image;

        // Getters
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getPrice() { return price; }
        public Times getTimes() { return times; }
        public Image getImage() { return image; }
    }

    public static class Times {
        @SerializedName("Main Story")
        private String mainStory;

        @SerializedName("Main + Sides")
        private String mainPlusSides;

        private String Completionist;

        @SerializedName("All Styles")
        private String allStyles;

        // Getters
        public String getMainStory() { return mainStory; }
        public String getMainPlusSides() { return mainPlusSides; }
        public String getCompletionist() { return Completionist; }
        public String getAllStyles() { return allStyles; }
    }

    public static class Image {
        private String url;
        private String alt;

        // Getter
        public String getUrl() { return url; }
    }
}