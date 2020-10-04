package spring.app.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "All details about the theme in DTO")
public class ThemeDto {

    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(notes = "Theme version")
    private Long version;

    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(notes = "Theme id")
    private Long id;

    @NotBlank
    @ApiModelProperty(notes = "Theme title")
    private String title;

    @NotNull
    @ApiModelProperty(notes = "Theme critical weight")
    private Integer criticalWeight;

    @Null(groups = CreateGroup.class)
    @Positive(groups = UpdateGroup.class)
    @ApiModelProperty(notes = "Theme position")
    private Integer position;

    @NotNull
    @ApiModelProperty(notes = "Review point")
    private Integer reviewPoint;

    @Null(groups=UpdateGroup.class)
    @NotNull(groups=CreateGroup.class)
    @ApiModelProperty(notes = "Theme type")
    private String type;

    public ThemeDto() {
    }

    public ThemeDto(Long version,
                    Long id,
                    String title,
                    Integer criticalWeight,
                    Integer position,
                    Integer reviewPoint) {
        this.version = version;
        this.id = id;
        this.title = title;
        this.criticalWeight = criticalWeight;
        this.position = position;
        this.reviewPoint = reviewPoint;
    }

    public ThemeDto(Long version,
                    Long id,
                    String title,
                    Integer criticalWeight,
                    Integer position,
                    Integer reviewPoint,
                    String type) {
        this.version = version;
        this.id = id;
        this.title = title;
        this.criticalWeight = criticalWeight;
        this.position = position;
        this.reviewPoint = reviewPoint;
        this.type = type;
    }

}
