package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookDaoImpl implements BookDao {

    private final JdbcTemplate jdbcTemplate;

    public BookDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> findAllBooksSortByTitle(Pageable pageable) {
        String sql = "SELECT * FROM book order by title " +
                pageable.getSort().getOrderFor("title").getDirection().name()
                + " limit ? offset ?";
        System.out.println(sql);
        return jdbcTemplate.query(sql, getRowMapper(), pageable.getPageSize(), pageable.getOffset());
    }

    @Override
    public List<Book> findAllBook(Pageable pageable) {
        return jdbcTemplate.query("SELECT * FROM book LIMIT ? OFFSET ?", getRowMapper(), pageable.getPageSize(), pageable.getOffset());
    }

    @Override
    public List<Book> findAllBook(int pageSize, int offset) {
        return jdbcTemplate.query("SELECT * FROM book LIMIT ? OFFSET ?", getRowMapper(), pageSize, offset);
    }

    @Override
    public List<Book> findAllBook() {
        return jdbcTemplate.query("SELECT * FROM book", getRowMapper());
    }

    @Override
    public Book getById(Long id) {
        //expect one return
        return jdbcTemplate.queryForObject("SELECT * FROM book WHERE id = ?", getRowMapper() ,id);
    }


    @Nullable
    @Override
    public Book findBookByTitle(String string) {
        List<Book> books = jdbcTemplate.query("SELECT * FROM Book WHERE title = ?", getRowMapper(), string);

        List<Book> book = new ArrayList<>();
        return book.get(0) != null ? book.get(0) : null;
    }

    @Override
    public Book saveNewBook(Book book) {
        jdbcTemplate.update("INSERT INTO BOOK (id, title, isbn, publisher) VALUES(?,?,?,?)",
                book.getId(), book.getTitle(), book.getIsbn(), book.getPublisher());

        return jdbcTemplate.queryForObject("SELECT * FROM Book WHERE id = ?",getRowMapper(), book.getId());
    }

    @Nullable
    @Override
    public Book updateBook(Book book) {
        jdbcTemplate.update("UPDATE Book SET title = ?, isbn = ?, publisher = ? WHERE id = ?",
                book.getTitle(), book.getIsbn(), book.getPublisher(), book.getId());

        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Book WHERE id = ?", getRowMapper(), book.getId());
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public void deleteBookById(Long id) {
        jdbcTemplate.update("DELETE FROM Book WHERE id = ?", id);
    }

    public BookMapper getRowMapper() {
        return new BookMapper();
    }
}
