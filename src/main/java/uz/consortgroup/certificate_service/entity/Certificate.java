package uz.consortgroup.certificate_service.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.consortgroup.certificate_service.constant.CertificateTemplate;
import uz.consortgroup.certificate_service.constant.TableName;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Builder
@Table(name = TableName.certificate,
        schema = "certificate_schema", indexes = {
        @Index(name = "idx_user_id", columnList = "listener_id"),
        @Index(name = "idx_reg_number", columnList = "serial_number"),
        @Index(name = "idx_certificate_course_id", columnList = "course_id")},
        uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_certificate_course_listener_date",
            columnNames = {"course_id", "listener_id", "issued_date"})}
)
public class Certificate {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "serial_number", nullable = false, unique = true)
    private String serialNumber;

    @Column(name = "course_id", nullable = false)
    private UUID courseId;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "listener_id", nullable = false)
    private UUID listenerId;

    @Column(name = "listener_full_name", nullable = false)
    private String listenerFullName;

    @Column(name = "score", nullable = false)
    private Double score;

    @Column(name = "issued_date", nullable = false, updatable = false)
    private Instant issuedDate;

    @Column(name = "expire_date")
    private Instant expiryDate;

    @Column(name = "certificate_template")
    private CertificateTemplate certificateTemplate;

    @Column(name = "upload_path")
    private String uploadPath;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "last_modified_at")
    private Instant lastModifiedAt;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @PrePersist
    public void onCreate() {
        this.issuedDate = Instant.now();
        this.createdAt = Instant.now();
    }
}
