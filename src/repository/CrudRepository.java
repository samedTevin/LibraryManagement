package repository;

import java.sql.SQLException;
import java.util.List;

public interface CrudRepository<T> {

    void add(T entity) throws SQLException;

    void update(T entity) throws SQLException;

    void delete(T entity) throws SQLException;

    T get(int id) throws SQLException;

    List<T> getAll() throws SQLException;

}
