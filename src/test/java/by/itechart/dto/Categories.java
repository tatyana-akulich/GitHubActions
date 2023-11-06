package by.itechart.dto;

public enum Categories {
    Horror ("Horror"),
    Action ("Action"),
    Strategy("Strategy"),
    Software("Software"),
    Survival("Survival"),
    Anime("Anime"),
    Adventure ("Adventure"),
    Space("Space");
    private String title;

    Categories(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
