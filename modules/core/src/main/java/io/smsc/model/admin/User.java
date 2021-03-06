package io.smsc.model.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.smsc.converters.CryptoConverter;
import io.smsc.model.BaseEntity;
import io.smsc.model.customer.Salutation;
import io.smsc.model.dashboard.Dashboard;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * Specifies User class as an entity class.
 *
 * @author Nazar Lipkovskyy
 * @see BaseEntity
 * @since 0.0.1-SNAPSHOT
 */
@Entity(name = "AdminUser")
@Table(name = "USER_ACCOUNT", uniqueConstraints = {@UniqueConstraint(columnNames = "USERNAME", name = "users_username_idx"),
        @UniqueConstraint(columnNames = "EMAIL", name = "users_email_idx")})
public class User extends BaseEntity {

    @Id
    @SequenceGenerator(name = "user_account_seq", sequenceName = "user_account_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_account_seq")
    @Column(name = "ID")
    // PROPERTY access for id due to bug: https://hibernate.atlassian.net/browse/HHH-3718
    @Access(value = AccessType.PROPERTY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "SALUTATION", nullable = false)
    @NotNull(message = "EMPTY_VALIDATION_ERROR")
    private Salutation salutation;

    @Column(name = "USERNAME", nullable = false, unique = true)
    @NotEmpty(message = "EMPTY_VALIDATION_ERROR")
    private String username;

    @Convert(converter = CryptoConverter.class)
    @Column(name = "PASSWORD", nullable = false)
    @NotEmpty(message = "EMPTY_VALIDATION_ERROR")
    @JsonIgnore
    private String password;

    @Column(name = "FIRST_NAME", nullable = false)
    @NotEmpty(message = "EMPTY_VALIDATION_ERROR")
    private String firstname;

    @Column(name = "SURNAME", nullable = false)
    @NotEmpty(message = "EMPTY_VALIDATION_ERROR")
    private String surname;

    @Column(name = "EMAIL", nullable = false, unique = true)
    @Email(message = "EMAIL_FORMAT_VALIDATION_ERROR")
    @NotEmpty(message = "EMPTY_VALIDATION_ERROR")
    private String email;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = true;

    @Column(name = "CREATED", nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date created = new Date();

    @Column(name = "BLOCKED", nullable = false)
    private Boolean blocked = false;

    @ManyToMany(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            targetEntity = Role.class)
    @JoinTable(
            name = "ADMIN_USER_ROLE_USER",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
    )
    @OrderBy("id asc")
    private Set<Role> roles;

    @ManyToMany(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            targetEntity = Group.class)
    @JoinTable(
            name = "ADMIN_USER_GROUP_USER",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")
    )
    @OrderBy("id asc")
    private Set<Group> groups;

    @ManyToMany(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            targetEntity = Authority.class)
    @JoinTable(
            name = "ADMIN_USER_AUTHORITY_USER",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")
    )
    @OrderBy("id asc")
    private Set<Authority> authorities;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @OrderBy("id asc")
    private Set<Dashboard> dashboards;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstName) {
        this.firstname = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surName) {
        this.surname = surName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public Set<Dashboard> getDashboards() {
        return dashboards;
    }

    public void setDashboards(Set<Dashboard> dashboards) {
        this.dashboards = dashboards;
    }

    public Salutation getSalutation() {
        return salutation;
    }

    public void setSalutation(Salutation salutation) {
        this.salutation = salutation;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!getId().equals(user.getId())) return false;
        if (getSalutation() != user.getSalutation()) return false;
        if (!getUsername().equals(user.getUsername())) return false;
        if (!getFirstname().equals(user.getFirstname())) return false;
        if (!getSurname().equals(user.getSurname())) return false;
        if (!getEmail().equals(user.getEmail())) return false;
        return getCreated().equals(user.getCreated());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getId());
        result = 31 * result + Objects.hashCode(getSalutation());
        result = 31 * result + Objects.hashCode(getUsername());
        result = 31 * result + Objects.hashCode(getFirstname());
        result = 31 * result + Objects.hashCode(getSurname());
        result = 31 * result + Objects.hashCode(getEmail());
        result = 31 * result + Objects.hashCode(getCreated());
        return result;
    }

    @Override
    public String toString() {
        return "{id = " + id +
                ", salutation = '" + salutation + '\'' +
                ", username = '" + username + '\'' +
                ", firstname = '" + firstname + '\'' +
                ", surname = '" + surname + '\'' +
                ", email = '" + email + '\'' +
                ", active = " + active +
                ", created = '" + created + '\'' +
                ", blocked = " + blocked +
                ", version = " + version +
                ", lastModifiedDate = '" + lastModifiedDate + '\'' +
                "}";
    }
}
