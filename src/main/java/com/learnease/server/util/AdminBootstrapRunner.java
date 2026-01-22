package com.learnease.server.util;

import com.learnease.server.model.Address;
import com.learnease.server.model.Admin;
import com.learnease.server.model.UserAuth;
import com.learnease.server.model.UserDetails;
import com.learnease.server.model.enums.Gender;
import com.learnease.server.model.enums.Role;
import com.learnease.server.model.enums.Status;
import com.learnease.server.repository.AdminRepository;
import com.learnease.server.repository.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AdminBootstrapRunner implements CommandLineRunner {
    private final Logger LOG = LoggerFactory.getLogger(AdminBootstrapRunner.class);
    private final AdminRepository adminRepository;
    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${app.admin_email}")
    private String adminEmail;
    @Value("${app.admin_pass}")
    private String adminPass;



    @Override
    public void run(String... args) throws Exception {
        UserAuth userAuth = userAuthRepository.findByEmailAndStatusAndRole(adminEmail, Status.ACTIVE, Role.ADMIN);
        if(userAuth==null){
            LOG.info("Default admin not found");
            LOG.info("Creating default admin email: " + adminEmail + "password: " + adminPass );

            UserAuth adminAuth = new UserAuth(
                    adminEmail,
                    passwordEncoder.encode(adminPass),
                    Role.ADMIN,
                    Status.ACTIVE
            );

            UserDetails adminDetails = new UserDetails()
                    .setFirstName("System")
                    .setLastName("Administrator")
                    .setDob(LocalDate.of(1990, 1, 1)) // valid past date
                    .setGender(Gender.MALE)
                    .setPhoneNo("9999999999") // reserved dummy number
                    .setProfilePic("https://dummyimage.com/300x300/admin")
                    .setUserAuth(adminAuth)
                    .setAddress(
                            new Address()
                                    .setAddressLine1("System Street")
                                    .setCity("System City")
                                    .setState("System State")
                                    .setCountry("System Country")
                                    .setPinCode("000000")
                    );

            Admin admin = new Admin()
                    .setUserDetails(adminDetails)
                    .setCreatedBy(null);
            adminRepository.save(admin);
            LOG.info("default admin created successfully!");
        }
    }
}
