package spring.app.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import spring.app.dto.QuestionDto;
import spring.app.model.Question;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface QuestionMapper {

    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    QuestionDto toDto(Question entity);

    Question toEntity(QuestionDto dto);

    List<QuestionDto> getAllQuestionDto (List<Question> questions);
}

