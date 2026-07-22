package library_management_system.mapper;

import library_management_system.dto.request.AuthorRequest;
import library_management_system.dto.response.AuthorResponse;
import library_management_system.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

	AuthorResponse toResponse(Author entity);

	@Mapping(target = "id", ignore = true)
	Author toEntity(AuthorRequest request);

	@Mapping(target = "id", ignore = true)
	void updateEntity(AuthorRequest request,@MappingTarget Author entity);
}
