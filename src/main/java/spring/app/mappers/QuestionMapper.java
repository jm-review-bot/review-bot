package spring.app.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import spring.app.dto.QuestionDto;
import spring.app.model.Question;

@Mapper
@Component
public interface QuestionMapper {

    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    @Mappings({
            @Mapping(target = "id" , source = "entity.id") ,
            @Mapping(target = "title" , source = "entity.title"),
            @Mapping(target = "position" , source = "entity.position"),
            @Mapping(target = "answer" , source = "entity.answer"),
            @Mapping(target = "weight" , source = "entity.weight")
    })
    QuestionDto toDto(Question entity);

    @Mappings({
            @Mapping(target = "id" , source = "dto.id") ,
            @Mapping(target = "title" , source = "dto.title") ,
            @Mapping(target = "position" , source = "dto.position"),
            @Mapping(target = "answer" , source = "dto.answer"),
            @Mapping(target = "weight" , source = "dto.weight")
    })
    Question toEntity(QuestionDto dto);
}
