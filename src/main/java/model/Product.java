package model;

public class Product {
    private int id;
    private String name;
    private String description;
    private String imageLink;

    public Product(int id, String name, String description, String imageLink){
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageLink  = imageLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageLink='" + imageLink + '\'' +
                '}';
    }
}
