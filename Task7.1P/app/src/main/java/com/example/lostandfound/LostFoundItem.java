package com.example.lostandfound;

// This class represents ONE lost/found item
// Every item saved in the database will become a LostFoundItem object in Java
public class LostFoundItem {

    // Fields — these match the database columns
    private int    id;
    private String type;        // "Lost" or "Found"
    private String name;        // e.g. "iPhone 14"
    private String phone;       // Contact number
    private String description;
    private String date;
    private String location;
    private String category;    // e.g. "Electronics"
    private String imageUri;    // Path to image file

    private String timestamp;   // ← ADD THIS


    // Empty constructor — required for creating blank objects
    public LostFoundItem() {}

    // Full constructor — fills all fields at once
    public LostFoundItem(String type, String name, String phone,
                         String description, String date,
                         String location, String category, String imageUri) {
        this.type        = type;
        this.name        = name;
        this.phone       = phone;
        this.description = description;
        this.date        = date;
        this.location    = location;
        this.category    = category;
        this.imageUri    = imageUri;
    }

    // ── Getters ─────────────────────────────────────────────────────────
    public int    getId()          { return id; }
    public String getType()        { return type; }
    public String getName()        { return name; }
    public String getPhone()       { return phone; }
    public String getDescription() { return description; }
    public String getDate()        { return date; }
    public String getLocation()    { return location; }
    public String getCategory()    { return category; }
    public String getImageUri()    { return imageUri; }

    public String getTimestamp()   { return timestamp; }   // ← ADD THIS


    // ── Setters ─────────────────────────────────────────────────────────
    public void setId(int id)                   { this.id = id; }
    public void setType(String type)             { this.type = type; }
    public void setName(String name)             { this.name = name; }
    public void setPhone(String phone)           { this.phone = phone; }
    public void setDescription(String desc)      { this.description = desc; }
    public void setDate(String date)             { this.date = date; }
    public void setLocation(String location)     { this.location = location; }
    public void setCategory(String category)     { this.category = category; }
    public void setImageUri(String imageUri)     { this.imageUri = imageUri; }

    public void setTimestamp(String timestamp) { this.timestamp = timestamp; } // ← ADD THIS

}
