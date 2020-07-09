package spring.app.mapper;

import org.mapstruct.Mapper;
import spring.app.dto.QuestionDto;
import spring.app.model.Question;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    Question questionDtoToQuestionEntity(QuestionDto questionDto);

    QuestionDto questionEntityToQuestionDto(Question question);
}
