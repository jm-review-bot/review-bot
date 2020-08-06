package spring.app.dto;

import lombok.Getter;
import lombok.Setter;
import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@Setter
public class FixedThemeDto {

    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private Long id;

    @NotBlank
    private String title;

    private Integer criticalWeight;

    @Null(groups = {CreateGroup.class, UpdateGroup.class})
    private Integer position;

    private Integer reviewPoint;

    public FixedThemeDto() {
    }

    public FixedThemeDto(Long id, String title, Integer criticalWeight, Integer position, Integer reviewPoint) {
        this.id = id;
        this.title = title;
        this.criticalWeight = criticalWeight;
        this.position = position;
        this.reviewPoint = reviewPoint;
    }
}
