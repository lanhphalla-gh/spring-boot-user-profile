package user.profile.configJWT

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import user.profile.authentication.login.repository.LoginRepository

@Service
class CustomUserDetailsService(
    private val loginRepository: LoginRepository
): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {

        // Find user from database
        val user = loginRepository.findByUsername(username)
            ?: throw UsernameNotFoundException(
                "User not found: $username"
            )

        // Return Spring Security UserDetails
        return User.withUsername(user.username!!)
            .password(user.password!!)
            .authorities(emptyList())
            .build()
    }
}