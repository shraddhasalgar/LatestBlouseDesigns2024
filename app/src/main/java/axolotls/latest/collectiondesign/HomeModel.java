package axolotls.latest.collectiondesign;

import java.io.Serializable;

public class HomeModel implements Serializable {
    private String id;
    private String name;
    private String img;

    private String favuoriteId;

    public HomeModel(String id, String name, String img, String favuoriteId) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.favuoriteId = favuoriteId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }
    public String getFavuoriteId() {
        return favuoriteId;
    }


    // Add a setter for the id field
    public void setId(String id) {
        this.id = id;
    }
}
