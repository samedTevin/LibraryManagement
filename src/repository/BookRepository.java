package repository;

import model.Book;
import util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    private Database database = new Database();



    public boolean add(Book book) {
        String sql = "INSERT INTO books(title, author, imageSrc, count, rating) VALUES(?, ?, ?, ?, ?)";
        try (Connection connection = database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getImageSrc());
            preparedStatement.setInt(4, book.getCount());
            preparedStatement.setInt(5, book.getRating());
            int rows = preparedStatement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, imageSrc = ?, count = ?, rating = ? WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getImageSrc());
            preparedStatement.setInt(4, book.getCount());
            preparedStatement.setInt(5, book.getRating());
            preparedStatement.setInt(6, book.getId());
            int rows = preparedStatement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int rows = preparedStatement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Book> getAll() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection connection = database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> search(String query) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ?";
        try (Connection connection = database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            String searchStr = "%" + query + "%";
            preparedStatement.setString(1, searchStr);
            preparedStatement.setString(2, searchStr);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    private Book extractBookFromResultSet(ResultSet rs) throws SQLException {
        return new Book(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getString("imageSrc"),
            rs.getInt("count"),
            rs.getInt("rating")
        );
    }
}
