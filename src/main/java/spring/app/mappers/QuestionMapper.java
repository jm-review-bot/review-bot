package spring.app.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import spring.app.dto.QuestionDto;
import spring.app.model.Question;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface QuestionMapper {

    QuestionDto toDto(Question entity);

    Question toEntity(QuestionDto dto);

    List<QuestionDto> getAllQuestionDto(List<Question> questions);
}

