package spring.app.dto;

import lombok.Getter;
import lombok.Setter;
import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;
import spring.app.model.Role;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@Setter
public class UserDto {

    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private long id;
    private String firstName;
    private String lastName;
    private int vkId;
    private int reviewPoint;
    private String role;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    public UserDto() {
    }

    public UserDto(Long id,
                   String firstName,
                   String lastName,
                   Integer vkId,
                   Integer reviewPoint,
                   String role,
                   boolean isAccountNonExpired,
                   boolean isAccountNonLocked,
                   boolean isCredentialsNonExpired,
                   boolean isEnabled) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.vkId = vkId;
        this.reviewPoint = reviewPoint;
        this.role = role;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }
}
