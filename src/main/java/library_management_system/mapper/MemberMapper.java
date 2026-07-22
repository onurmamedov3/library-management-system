package library_management_system.mapper;

import library_management_system.dto.request.MemberRequest;
import library_management_system.dto.response.MemberResponse;
import library_management_system.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MemberMapper {

	MemberResponse toResponse(Member entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "active", ignore = true)
	Member toEntity(MemberRequest request);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "active", ignore = true)
	void updateEntity(MemberRequest request, @MappingTarget Member entity);
}
