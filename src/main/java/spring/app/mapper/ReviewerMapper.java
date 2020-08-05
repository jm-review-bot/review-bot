package spring.app.mapper;

import org.mapstruct.Mapper;
import spring.app.dto.ReviewerDto;
import spring.app.model.User;

@Mapper(componentModel = "spring")
public interface ReviewerMapper {
    ReviewerDto userToReviewerDto (User user) ;
    User reviewerDtoToUser (ReviewerDto reviewerDto);
}
