package ac.umn.id.nulis.HelperClass;

public class ChapterDetail {
    String chapterDetailId, title, content;

    public ChapterDetail() {
    }
    public ChapterDetail(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public ChapterDetail(String title, String content, String chapterDetailId) {
        this.title = title;
        this.content = content;
        this.chapterDetailId = chapterDetailId;
    }

    public String getCDetailId() {
        return chapterDetailId;
    }

    public String getChapterDetailTitle() {
        return title;
    }

    public String getChapterDetailContent() {
        return content;
    }

}