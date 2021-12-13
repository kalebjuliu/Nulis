package ac.umn.id.nulis.HelperClass;

public class Location {
    String lId, title, description, history, design;
    public Location(){}
    public Location(String title, String description, String history, String design){
        this.title = title;
        this.description = description;
        this.history = history;
        this.design = design;
    }
    public Location(String title, String description, String history, String design, String lId){
        this.title = title;
        this.description = description;
        this.history = history;
        this.design = design;
        this.lId = lId;
    }

    public String getTitle() {
        return title;
    }

    public String getlId() {
        return lId;
    }

    public String getDescription() {
        return description;
    }

    public String getHistory() {
        return history;
    }

    public String getDesign() {
        return design;
    }
}
