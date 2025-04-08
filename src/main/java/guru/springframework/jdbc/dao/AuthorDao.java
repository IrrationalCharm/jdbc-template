package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorDao {

    Author getById(Long id);

    List<Author> findAllByLastNameSortByFirstName(String lastName, Pageable pageable);

    List<Author> findAllAuthorsByLastName(String lastName, Pageable pageable);

    Author findAuthorByName(String first_name, String last_name);

    Author saveNewAuthor(Author author);

    Author updateAuthor(Author author);

    void deleteAuthorById(Long id);
}
