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
public class UserDto {

    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(notes = "User's ID.")
    private Long id;

    @Null
    @ApiModelProperty(notes = "User's (Integer) VK-ID.")
    private Integer vkId;

    @NotNull
    @ApiModelProperty(notes = "User's (String) VK-ID.")
    private String stringVkId;

    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(notes = "User's first and last names.")
    private String name;

    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(notes = "User's role.")
    private String role;

    @Null(groups = UpdateGroup.class)
    @NotNull(groups = CreateGroup.class)
    @ApiModelProperty(notes = "Start theme position for new user.")
    Integer startThemePosition;

    public UserDto() {
    }

    public UserDto(Long id,
                   Integer vkId,
                   String firstName,
                   String lastName,
                   String role) {
        this.id = id;
        this.vkId = vkId;
        this.name = firstName + " " + lastName;
        this.role = role;
    }
}
