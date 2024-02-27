package hr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    protected String createdBy;

    @Column(name = "modified_at")
    protected LocalDateTime modifiedAt;

    @Column(name = "modified_by")
    protected String modifiedBy;

}
