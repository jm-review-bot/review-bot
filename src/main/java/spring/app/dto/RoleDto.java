package spring.app.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@Setter
@ApiModel(description = "All details about user in DTO")
public class RoleDto {

    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(notes = "Role ID")
    private Long id;

    @NotNull
    @ApiModelProperty(notes = "Role name")
    private String name;

    public RoleDto(@Null(groups = CreateGroup.class) @NotNull(groups = UpdateGroup.class) Long id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }
}
