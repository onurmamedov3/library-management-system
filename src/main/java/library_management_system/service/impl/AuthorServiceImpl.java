package library_management_system.service.impl;

import library_management_system.dto.request.AuthorRequest;
import library_management_system.dto.response.AuthorResponse;
import library_management_system.entity.Author;
import library_management_system.exception.ResourceAlreadyExistsException;
import library_management_system.exception.ResourceNotFoundException;
import library_management_system.mapper.AuthorMapper;
import library_management_system.repository.AuthorRepository;
import library_management_system.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

	private final AuthorRepository authorRepository;

	private final AuthorMapper authorMapper;

	@Override
	public Page<AuthorResponse> getAll(Pageable pageable){
		return authorRepository.findAll(pageable).map(authorMapper::toResponse);
	}

	@Override
	public AuthorResponse getById(UUID uuid){
		Author author = authorRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("Author with " + uuid + " not found"));
		return authorMapper.toResponse(author);
	}

	@Override
	public AuthorResponse create(AuthorRequest request){

		if(authorRepository.existsByEmail(request.email())){
			throw new ResourceAlreadyExistsException("Author with " + request.email() + " already exists");
		}

		Author author = authorMapper.toEntity(request);

		authorRepository.save(author);

		return authorMapper.toResponse(author);
	}

	@Override
	public AuthorResponse update(String email, AuthorRequest request){
		Author author =  authorRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Author with " + email + " does not exist"));

		authorMapper.updateEntity(request, author);

		authorRepository.save(author);

		return authorMapper.toResponse(author);
	}

	@Override
	public void delete(String email){
		Author author =  authorRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Author with " + email + " does not exist"));

		author.setActive(false);

		authorRepository.save(author);
	}

}
