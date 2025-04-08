package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;

public interface AuthorDao {

    Author getById(Long id);

    Author findAuthorByName(String first_name, String last_name);

    Author saveNewAuthor(Author author);

    Author updateAuthor(Author author);

    void deleteAuthorById(Long id);
}
