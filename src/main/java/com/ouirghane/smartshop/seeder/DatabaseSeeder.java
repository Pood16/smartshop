package com.ouirghane.smartshop.seeder;


import com.ouirghane.smartshop.entity.User;
import com.ouirghane.smartshop.enums.UserRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (isDatabaseEmpty()) {
            seedUsers();
        }
    }

    private boolean isDatabaseEmpty() {
        Long userCount = entityManager.createQuery("SELECT COUNT(u) FROM User u", Long.class)
                .getSingleResult();
        return userCount == 0;
    }

    private void seedUsers() {
        User admin1 = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .role(UserRole.ADMIN)
                .build();
        entityManager.persist(admin1);
    }


}