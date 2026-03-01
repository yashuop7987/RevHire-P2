package com.application.portal;

import com.application.portal.Model.Employer;
import com.application.portal.Model.Role;
import com.application.portal.Model.User;
import com.application.portal.Repository.EmployerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class EmployerRepositoryTest {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private TestEntityManager entityManager;

    // 🔹 Helper method to create User
    private User createUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("password123");
        user.setRole(Role.valueOf("ROLE_EMPLOYER"));
        return entityManager.persistAndFlush(user);
    }

    // ✅ Test saving employer
    @Test
    @DisplayName("Should save employer successfully")
    void testSaveEmployer() {

        User user = createUser("companyUser");

        Employer employer = Employer.builder()
                .companyName("TechNova")
                .industry("IT")
                .companySize("100-500")
                .website("www.technova.com")
                .location("Pune")
                .email("hr@technova.com")
                .description("Leading IT company")
                .user(user)
                .build();

        Employer savedEmployer = employerRepository.save(employer);

        assertThat(savedEmployer.getId()).isNotNull();
        assertThat(savedEmployer.getCompanyName()).isEqualTo("TechNova");
        assertThat(savedEmployer.getUser()).isEqualTo(user);
    }

    // ✅ Test find by ID
    @Test
    @DisplayName("Should find employer by id")
    void testFindEmployerById() {

        User user = createUser("employerUser");

        Employer employer = Employer.builder()
                .companyName("NextGen")
                .industry("Software")
                .companySize("50-100")
                .website("www.nextgen.com")
                .location("Mumbai")
                .email("contact@nextgen.com")
                .description("Software Solutions Company")
                .user(user)
                .build();

        Employer saved = entityManager.persistAndFlush(employer);

        Employer found = employerRepository.findById(saved.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("contact@nextgen.com");
    }

    // ✅ Test unique email constraint
    @Test
    @DisplayName("Should not allow duplicate email")
    void testUniqueEmailConstraint() {

        User user1 = createUser("user1");
        User user2 = createUser("user2");

        Employer employer1 = Employer.builder()
                .companyName("CompanyOne")
                .email("duplicate@email.com")
                .user(user1)
                .build();

        Employer employer2 = Employer.builder()
                .companyName("CompanyTwo")
                .email("duplicate@email.com")
                .user(user2)
                .build();

        employerRepository.save(employer1);

        assertThrows(Exception.class, () -> {
            employerRepository.saveAndFlush(employer2);
        });
    }
}