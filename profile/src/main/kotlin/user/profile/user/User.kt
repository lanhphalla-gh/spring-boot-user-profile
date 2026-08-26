package user.profile.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import user.profile.role.Role
import user.profile.rolepermission.dto.RolePermissionRequest
import java.util.UUID

@Entity
@Table(name = "users")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null

    @Column(nullable = false, unique = true, length = 50)
    var username: String? = null

    @Column(nullable = false, unique = true, length = 100)
    var email: String? = null

    @Column(nullable = false, length = 255)
    var password: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    var role: Role? = null
}