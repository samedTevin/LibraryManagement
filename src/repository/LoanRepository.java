package repository;

import model.Loan;
import util.Database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanRepository {
    private final Database database = new Database();



    public boolean borrowBook(int memberId, int bookId) {
        String insertLoan = "INSERT INTO loans (member_id, book_id, borrow_date, status) VALUES (?, ?, ?, 'BORROWED')";
        String updateBook = "UPDATE books SET count = count - 1 WHERE id = ? AND count > 0";
        
        try (Connection connection = database.getConnection()) {
            connection.setAutoCommit(false);
            
            try (PreparedStatement updateStmt = connection.prepareStatement(updateBook)) {
                updateStmt.setInt(1, bookId);
                int rows = updateStmt.executeUpdate();
                if (rows == 0) {
                    connection.rollback();
                    return false; // Book not available or doesn't exist
                }
            }
            
            try (PreparedStatement insertStmt = connection.prepareStatement(insertLoan)) {
                insertStmt.setInt(1, memberId);
                insertStmt.setInt(2, bookId);
                insertStmt.setDate(3, Date.valueOf(LocalDate.now()));
                insertStmt.executeUpdate();
            }
            
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean returnBook(int loanId, int bookId) {
        String updateLoan = "UPDATE loans SET status = 'RETURNED', return_date = ? WHERE id = ?";
        String updateBook = "UPDATE books SET count = count + 1 WHERE id = ?";
        
        try (Connection connection = database.getConnection()) {
            connection.setAutoCommit(false);
            
            try (PreparedStatement uLoanStmt = connection.prepareStatement(updateLoan)) {
                uLoanStmt.setDate(1, Date.valueOf(LocalDate.now()));
                uLoanStmt.setInt(2, loanId);
                uLoanStmt.executeUpdate();
            }
            
            try (PreparedStatement updateStmt = connection.prepareStatement(updateBook)) {
                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();
            }
            
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Loan> getActiveLoans() {
        return getLoansByStatus("BORROWED");
    }

    private List<Loan> getLoansByStatus(String status) {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.id, l.member_id, l.book_id, l.borrow_date, l.return_date, l.status, " +
                     "m.first_name, m.last_name, b.title " +
                     "FROM loans l " +
                     "JOIN members m ON l.member_id = m.id " +
                     "JOIN books b ON l.book_id = b.id " +
                     "WHERE l.status = ?";
                     
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Loan loan = new Loan();
                    loan.setId(rs.getInt("id"));
                    loan.setMemberId(rs.getInt("member_id"));
                    loan.setBookId(rs.getInt("book_id"));
                    if (rs.getDate("borrow_date") != null) {
                        loan.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
                    }
                    if (rs.getDate("return_date") != null) {
                        loan.setReturnDate(rs.getDate("return_date").toLocalDate());
                    }
                    loan.setStatus(rs.getString("status"));
                    loan.setMemberName(rs.getString("first_name") + " " + rs.getString("last_name"));
                    loan.setBookTitle(rs.getString("title"));
                    loans.add(loan);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }
}
