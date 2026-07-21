package library_management_system.mapper;

import library_management_system.dto.request.BookRequest;
import library_management_system.dto.response.BookResponse;
import library_management_system.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {

	BookResponse toResponse(Book entity);

	@Mapping(target = "id", ignore = true)
	Book toEntity(BookRequest request);

	@Mapping(target = "id", ignore = true)
	void updateEntity(BookRequest request, @MappingTarget Book entity);
}
