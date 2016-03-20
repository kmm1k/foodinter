package ee.ttu.foodinter;

/**
 * Created by kmm on 20.03.2016.
 */
public class FoodPlace {
    private String name;
    private String location;
    private String category;
    private String url;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
