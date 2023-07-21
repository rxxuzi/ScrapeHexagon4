package data;

public class Data {
    private int id;
    private String tag;
    private int posts;
    private String category;
    private transient String trans;

    public Data(int id, String name, int posts, String category) {
        this.id = id;
        this.tag = name;
        this.posts = posts;
        this.category = category;
    }
    public Data(int id , String name) {
        this.id = id;
        this.tag = name;
    }

    public Data(int tagCounter, String tag, int posts, String general, String category) {
    }

    public int getId() {
        return id;
    }
    public String getTag() {
        return tag;
    }
    public int getPosts() {
        return posts;
    }
    public String getCategory() {
        return category;
    }
    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", posts=" + posts + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
