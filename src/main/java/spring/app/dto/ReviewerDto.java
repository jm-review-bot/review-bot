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
}
