package org.example.spring_crud_activity.domain;

public class ClubActivity {

    private Long id;
    private String title;
    private String clubName;
    private String category;
    private String activityDate;
    private String location;
    private Integer participants;
    private String description;

    public ClubActivity() {
    }

    public ClubActivity(Long id, String title, String clubName, String category,
                        String activityDate, String location,
                        Integer participants, String description) {
        this.id = id;
        this.title = title;
        this.clubName = clubName;
        this.category = category;
        this.activityDate = activityDate;
        this.location = location;
        this.participants = participants;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getParticipants() {
        return participants;
    }

    public void setParticipants(Integer participants) {
        this.participants = participants;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}