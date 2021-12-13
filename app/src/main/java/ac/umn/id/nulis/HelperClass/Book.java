package ac.umn.id.nulis.HelperClass;

public class Book {
    String bId, title, desc;

    public Book(){}
    public Book(String title, String desc){
        this.title = title;
        this.desc = desc;
    }
    public Book(String title, String desc, String bId){
        this.title = title;
        this.desc = desc;
        this.bId = bId;
    }

    public String getbId() { return bId; }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }
}
