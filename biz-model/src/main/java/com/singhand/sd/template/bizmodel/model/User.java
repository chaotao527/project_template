package com.singhand.sd.template.bizmodel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "t_user")
@Builder
@Schema
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("id")
    private Long ID;

    @Schema(description = "用户名")
    @Column(nullable = false)
    @Comment("用户名")
    private String name;
    @ManyToOne
    private Company company;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Role> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        User v = (User) o;
        return ID != null && Objects.equals(ID, v.ID);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
