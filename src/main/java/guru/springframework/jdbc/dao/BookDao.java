package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookDao {

    List<Book> findAllBooksSortByTitle(Pageable pageable);

    List<Book> findAllBook(Pageable pageable);

    //paging manually
    List<Book> findAllBook(int pageSize, int offset);

    List<Book> findAllBook();

    Book getById(Long id);

    Book findBookByTitle(String string);

    Book saveNewBook(Book book);

    Book updateBook(Book book);

    void deleteBookById(Long id);
}
