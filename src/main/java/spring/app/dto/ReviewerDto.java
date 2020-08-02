package spring.app.dto;

import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class ReviewerDto {

    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private long id;

    @NotBlank(groups = CreateGroup.class)
    private String firstName;

    @NotBlank(groups = CreateGroup.class)
    private String lastName;

    private Integer vkId;
    private boolean isViewed;
    private Integer reviewPoint;

    public ReviewerDto () {}

    public ReviewerDto (long id , String firstName , String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setId (long id) {
        this.id = id;
    }

    public void setFirstName (String firstName) {
        this.firstName = firstName;
    }

    public void setLastName (String lastName) {
        this.lastName = lastName;
    }

    public void setVkId (Integer vkId) {
        this.vkId = vkId;
    }

    public void setViewed(boolean viewed) {
        isViewed = viewed;
    }

    public void setReviewPoint(Integer reviewPoint) {
        this.reviewPoint = reviewPoint;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getVkId() {
        return vkId;
    }

    public boolean isViewed() {
        return isViewed;
    }

    public Integer getReviewPoint() {
        return reviewPoint;
    }
}
