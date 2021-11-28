package umn.ac.id.nulis.HelperClass;

public class Location {
    String lId, title;
    public Location(){}
    public Location(String title){
        this.title = title;
    }
    public Location(String title, String lId){
        this.title = title;
        this.lId = lId;
    }

    public String getcId() {
        return lId;
    }

    public String getTitle() {
        return title;
    }


}
