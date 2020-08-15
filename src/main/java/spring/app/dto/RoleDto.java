package spring.app.dto;

import lombok.Getter;
import lombok.Setter;
import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@Setter
public class RoleDto {

    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private long id;

    private String name;

    public RoleDto() {
    }

    public RoleDto(Long id,
                   String name) {
        this.id = id;
        this.name = name;
    }
}
