package library_management_system.service.impl;

import library_management_system.dto.request.MemberRequest;
import library_management_system.dto.response.MemberResponse;
import library_management_system.entity.Member;
import library_management_system.exception.ResourceAlreadyExistsException;
import library_management_system.exception.ResourceNotFoundException;
import library_management_system.mapper.MemberMapper;
import library_management_system.repository.MemberRepository;
import library_management_system.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;

	private final MemberMapper memberMapper;

	@Override
	public Page<MemberResponse> getAll(Pageable pageable) {
		return memberRepository.findAll(pageable).map(memberMapper::toResponse);
	}

	@Override
	public MemberResponse getById(UUID id) {
		Member member = memberRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));

		return memberMapper.toResponse(member);
	}

	@Override
	public MemberResponse create(MemberRequest request) {

		if (memberRepository.existsByEmail(request.email())) {
			throw new ResourceAlreadyExistsException("Member with email " + request.email() + " already exists");
		}

		Member member = memberMapper.toEntity(request);
		member.setActive(true);

		memberRepository.save(member);

		return memberMapper.toResponse(member);
	}

	@Override
	public MemberResponse update(String email, MemberRequest request) {
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Member with email " + email + " does not exist"));

		memberMapper.updateEntity(request, member);

		memberRepository.save(member);

		return memberMapper.toResponse(member);
	}

	@Override
	public void delete(String email) {
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Member with email " + email + " does not exist"));

		member.setActive(false);

		memberRepository.save(member);
	}
}
