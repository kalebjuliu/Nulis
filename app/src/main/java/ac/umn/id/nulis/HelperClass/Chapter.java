package ac.umn.id.nulis.HelperClass;

public class Chapter {
    String cId, title;
    public Chapter(){}
    public Chapter(String title){
        this.title = title;
    }
    public Chapter(String title, String cId){
        this.title = title;
        this.cId = cId;
    }

    public String getcId() {
        return cId;
    }

    public String getTitle() {
        return title;
    }
}
