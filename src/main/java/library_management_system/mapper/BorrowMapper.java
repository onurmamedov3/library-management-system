package library_management_system.mapper;

import library_management_system.dto.response.BorrowResponse;
import library_management_system.entity.Borrow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BorrowMapper {

	@Mapping(source = "member.id", target = "memberId")
	@Mapping(expression = "java(entity.getMember().getFirstName() + \" \" + entity.getMember().getLastName())", target = "memberName")
	@Mapping(source = "book.id", target = "bookId")
	@Mapping(source = "book.title", target = "bookTitle")
	BorrowResponse toResponse(Borrow entity);
}
