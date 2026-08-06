package library_management_system.repository.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import library_management_system.dto.request.BookFilterRequest;
import library_management_system.entity.Author;
import library_management_system.entity.Book;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class BookSpecification {


	public static Specification<Book> fromFilter(BookFilterRequest filter) {
		return Specification
				.where(isActive())
				.and(titleContains(filter.title()))
				.and(isbnStartsWith(filter.isbn()))
				.and(authorNameContains(filter.authorName()))
				.and(publishedAfter(filter.publishedFrom()))
				.and(publishedBefore(filter.publishedTo()));
	}

	private static Specification<Book> isActive() {
		return ((root, query, cb) -> cb.isTrue(root.get("isActive")));
	}

	private static Specification<Book> titleContains(String title) {
		if(title == null || title.isBlank()) return null;
		return ((root, query, cb) ->
				cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
	}

	private static Specification<Book> isbnStartsWith(String isbn) {
		if(isbn == null || isbn.isBlank()) return null;
		return ((root, query, cb) ->
				cb.like(root.get("isbn"), isbn + "%"));
	}

	private static Specification<Book> authorNameContains(String authorName) {
		if (authorName == null || authorName.isBlank()) return null;
		return (root, query, cb) -> {
			query.distinct(true);
			Join<Book, Author> authors = root.join("authors", JoinType.INNER);
			String pattern = "%" + authorName.toLowerCase() + "%";
			return cb.or(
					cb.like(cb.lower(authors.get("firstName")), pattern),
					cb.like(cb.lower(authors.get("lastName")), pattern)
			);
		};
	}

	private static Specification<Book> publishedAfter(LocalDate from) {
		if (from == null) return null;
		return (root, query, cb) ->
				cb.greaterThanOrEqualTo(root.get("publicationDate"), from);
	}

	private static Specification<Book> publishedBefore(LocalDate to) {
		if (to == null) return null;
		return (root, query, cb) ->
				cb.lessThanOrEqualTo(root.get("publicationDate"), to);
	}
}
