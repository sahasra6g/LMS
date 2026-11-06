package com.library.management.entity;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "borrow_records")
public class BorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrow_id")
    private Integer borrowId;

    @Column(name = "student_id", nullable = false)
    private Integer studentId;

    @Column(name = "book_id", nullable = false)
    private Integer bookId;

    @Column(name = "issued_by", nullable = false)
    private Integer issuedBy;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(length = 20)
    private String status;

    @PrePersist
    void beforeInsert() {
        if (issueDate == null) issueDate = LocalDate.now();
        if (status == null) status = "BORROWED";
    }

    public Integer getBorrowId() { return borrowId; }
    public Integer getStudentId() { return studentId; }
    public Integer getBookId() { return bookId; }
    public Integer getIssuedBy() { return issuedBy; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public String getStatus() { return status; }
    public void setBorrowId(Integer borrowId) { this.borrowId = borrowId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }
    public void setIssuedBy(Integer issuedBy) { this.issuedBy = issuedBy; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public void setStatus(String status) { this.status = status; }
}
