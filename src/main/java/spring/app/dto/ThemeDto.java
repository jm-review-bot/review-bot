package spring.app.dto;

import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class ThemeDto {

    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private Long id;

    @NotBlank
    private String title;

    @NotNull
    private Integer criticalWeight;

    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private Integer position;

    @NotNull
    private Integer reviewPoint;

    @NotBlank
    private String type;

    public ThemeDto() {
    }

    public ThemeDto(Long id,
                    String title,
                    Integer criticalWeight,
                    Integer position,
                    Integer reviewPoint) {
        this.id = id;
        this.title = title;
        this.criticalWeight = criticalWeight;
        this.position = position;
        this.reviewPoint = reviewPoint;
    }

    public ThemeDto(Long id,
                    String title,
                    Integer criticalWeight,
                    Integer position,
                    Integer reviewPoint,
                    String type) {
        this.id = id;
        this.title = title;
        this.criticalWeight = criticalWeight;
        this.position = position;
        this.reviewPoint = reviewPoint;
        this.type = type;
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

    public Integer getCriticalWeight() {
        return criticalWeight;
    }

    public void setCriticalWeight(Integer criticalWeight) {
        this.criticalWeight = criticalWeight;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getReviewPoint() {
        return reviewPoint;
    }

    public void setReviewPoint(Integer reviewPoint) {
        this.reviewPoint = reviewPoint;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
