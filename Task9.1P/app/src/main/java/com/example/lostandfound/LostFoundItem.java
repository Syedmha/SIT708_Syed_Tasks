package com.example.lostandfound;

public class LostFoundItem {

    private int    id;
    private String type;
    private String name;
    private String phone;
    private String description;
    private String date;
    private String location;
    private String category;
    private String imageUri;
    private String timestamp;
    private double latitude;   // NEW — GPS coordinate
    private double longitude;  // NEW — GPS coordinate

    public LostFoundItem() {}

    public LostFoundItem(String type, String name, String phone,
                         String description, String date, String location,
                         String category, String imageUri) {
        this.type = type;   this.name = name;     this.phone = phone;
        this.description = description;           this.date = date;
        this.location = location;                 this.category = category;
        this.imageUri = imageUri;
    }

    // ── Getters ──────────────────────────────────────────────────
    public int    getId()          { return id; }
    public String getType()        { return type; }
    public String getName()        { return name; }
    public String getPhone()       { return phone; }
    public String getDescription() { return description; }
    public String getDate()        { return date; }
    public String getLocation()    { return location; }
    public String getCategory()    { return category; }
    public String getImageUri()    { return imageUri; }
    public String getTimestamp()   { return timestamp; }
    public double getLatitude()    { return latitude; }   // NEW
    public double getLongitude()   { return longitude; }  // NEW

    // ── Setters ──────────────────────────────────────────────────
    public void setId(int id)                    { this.id = id; }
    public void setType(String t)                { this.type = t; }
    public void setName(String n)                { this.name = n; }
    public void setPhone(String p)               { this.phone = p; }
    public void setDescription(String d)         { this.description = d; }
    public void setDate(String d)                { this.date = d; }
    public void setLocation(String l)            { this.location = l; }
    public void setCategory(String c)            { this.category = c; }
    public void setImageUri(String u)            { this.imageUri = u; }
    public void setTimestamp(String t)           { this.timestamp = t; }
    public void setLatitude(double lat)          { this.latitude = lat; }   // NEW
    public void setLongitude(double lng)         { this.longitude = lng; }  // NEW
}
