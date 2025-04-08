package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorDaoImpl implements AuthorDao {

    private final JdbcTemplate jdbcTemplate;

    public AuthorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Author getById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM author WHERE id = ?", getRowMapper(), id);
    }

    @Override
    public List<Author> findAllByLastNameSortByFirstName(String lastName, Pageable pageable) {
        String sql = "SELECT * FROM author WHERE last_name = ? " +
                Sort.by("first_name").ascending();

        return jdbcTemplate.query(sql, getRowMapper(), pageable.getPageSize(), pageable.getOffset());
    }

    @Override
    public List<Author> findAllAuthorsByLastName(String lastName, Pageable pageable) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM author WHERE last_name = ?");

        if (pageable.getSort().getOrderFor("firstname") != null) {

            builder.append("ORDER BY first_name").append(
                    pageable.getSort().getOrderFor("firstname").getDirection().name()
            );
        }

        builder.append(" LIMIT ? OFFSET ?");

        return jdbcTemplate.query(builder.toString(), getRowMapper(), lastName, pageable.getPageSize(), pageable.getOffset());
    }

    @Override
    public Author findAuthorByName(String first_name, String last_name) {
        return jdbcTemplate.queryForObject("SELECT * FROM author WHERE first_name = ? AND last_name = ?", getRowMapper(), first_name, last_name);
    }

    @Override
    public Author saveNewAuthor(Author author) {
        jdbcTemplate.update("INSERT INTO author (first_name, last_name) VALUES (?,?)", author.getFirstName(), author.getLastName());
        Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        return this.getById(createdId);
    }

    @Override
    public Author updateAuthor(Author author) {
        jdbcTemplate.update("UPDATE author SET first_name = ?, last_name = ? WHERE id = ?", author.getFirstName(), author.getLastName(), author.getId());

        return this.getById(author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
        jdbcTemplate.update("DELETE FROM author WHERE id = ?", id);
    }

    private RowMapper<Author> getRowMapper() {
       return new AuthorMapper();
    }
}
