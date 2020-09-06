package spring.app.dto;

import lombok.Getter;
import lombok.Setter;
import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@Setter
public class UserDto {

    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private Long id;

    @Null
    private Integer vkId;

    @NotNull
    private String stringVkId;

    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private String name;

    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private String role;

    @Null(groups = UpdateGroup.class)
    @NotNull(groups = CreateGroup.class)
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
