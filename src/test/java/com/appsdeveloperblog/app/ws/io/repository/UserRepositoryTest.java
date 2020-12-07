package com.appsdeveloperblog.app.ws.io.repository;

import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

    // real in db √ 会真的在db内形成多条记录
    @Autowired
    UserRepository userRepository;

    static boolean recordsCreated = false;

    @BeforeEach
    void setUp() {
       if (!recordsCreated) createRecords();
    }

    private void createRecords(){
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("Hao");
        userEntity.setLastName("Qin");
        userEntity.setUserId("1a2b3c1");
        userEntity.setEncryptedPassword("xxx");
        userEntity.setEmail("test1@test.com");
        userEntity.setEmailVerificationStatus(true);

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setType("shipping");
        addressEntity.setAddressId("1231rda1");
        addressEntity.setCity("Vancouver");
        addressEntity.setCountry("Canada");
        addressEntity.setPostalCode("ABCCBA");
        addressEntity.setStreetName("123 Street Address");

        List<AddressEntity> addressEntityList = new ArrayList<>();
        addressEntityList.add(addressEntity);
        userEntity.setAddresses(addressEntityList);
        userRepository.save(userEntity);









        UserEntity userEntity2 = new UserEntity();
        userEntity2.setFirstName("Hao");
        userEntity2.setLastName("Qin");
        userEntity2.setUserId("1a2b3c");
        userEntity2.setEncryptedPassword("xxx");
        userEntity2.setEmail("test@test.com");
        userEntity2.setEmailVerificationStatus(true);

        AddressEntity addressEntity2 = new AddressEntity();
        addressEntity2.setType("shipping");
        addressEntity2.setAddressId("1231rda");
        addressEntity2.setCity("Vancouver");
        addressEntity2.setCountry("Canada");
        addressEntity2.setPostalCode("ABCCBA");
        addressEntity2.setStreetName("123 Street Address");

        List<AddressEntity> addressEntityList2 = new ArrayList<>();
        addressEntityList2.add(addressEntity2);
        userEntity2.setAddresses(addressEntityList2);

        userRepository.save(userEntity2);
        recordsCreated = true;
    }

    @Test
    void findAllUSersWithConfirmedEmailAddress() {
        Pageable pageable = PageRequest.of(0,2);
        Page<UserEntity> pages = userRepository.findAllUSersWithConfirmedEmailAddress(pageable);
        assertNotNull(pages);

        List<UserEntity> userEntities = pages.getContent();
        assertNotNull(userEntities);
        System.out.println(userEntities.size());
        assertTrue(userEntities.size() == 2);
    }

    @Test
    void findUserByFirstName() {
        String firstName = "Hao";
        List<UserEntity> users = userRepository.findUserByFirstName(firstName);
        assertNotNull(users);
        assertTrue(users.size() == 2);

        UserEntity user = users.get(0);
        assertTrue(user.getFirstName().equals(firstName));
    }



    @Test
    void findUserByLastName() {
        String lastName = "Qin";
        List<UserEntity> users = userRepository.findUserByLastName(lastName);
        assertNotNull(users);
        assertTrue(users.size() == 2);

        UserEntity user = users.get(0);
        assertTrue(user.getLastName().equals(lastName));
    }

    @Test
    void findUsersByKeyword() {
        String keyword = "H";
        List<UserEntity> users = userRepository.findUsersByKeyword(keyword);
        assertNotNull(users);
        assertTrue(users.size() == 2);

        UserEntity user = users.get(0);
        assertTrue(user.getLastName().contains(keyword) || user.getFirstName().contains(keyword));
    }

    @Test
    void findUserFirstNameAndLastNameByKeyword() {
        String keyword = "H";
        List<Object[]> users = userRepository.findUserFirstNameAndLastNameByKeyword(keyword);
        assertNotNull(users);
        assertTrue(users.size()==2);

        Object[] user = users.get(0);

        String userFirstName = String.valueOf(user[0]);
        String userLastName = String.valueOf(user[1]);

        assertNotNull(userFirstName);
        assertNotNull(userLastName);

        System.out.println("FirstName = " + userFirstName);
        System.out.println("LastName = " + userLastName);
    }

    @Test
    void updateUserEmailVerificationStatus() {
        boolean newEmailVerificationStatus = true;
        userRepository.updateUserEmailVerificationStatus(newEmailVerificationStatus, "1a2b3c");
        UserEntity storedUserDetails = userRepository.findByUserId("1a2b3c");
        boolean storedEmailVerificationStatus = storedUserDetails.getEmailVerificationStatus();
        assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);
    }

    @Test
    void findUserEntityByUserId() {
        String userId = "1a2b3c";
        UserEntity userEntity = userRepository.findUserEntityByUserId(userId);

        assertNotNull(userEntity);
        assertTrue(userEntity.getUserId().equals(userId));
    }

    @Test
    void findUserEntityFullNameByUserId() {
        String userId = "1a2b3c";
        List<Object[]> records = userRepository.getUserEntityFullNameByUserId(userId);

        assertNotNull(records);
        assertTrue(records.size() == 1);

        Object[] userDetails = records.get(0);

        String firstName = String.valueOf(userDetails[0]);
        String lastName = String.valueOf(userDetails[1]);

        assertNotNull(firstName);
        assertNotNull(lastName);
    }

    @Test
    void updateUserEntityEmailVerificationStatus() {
        boolean newEmailVerificationStatus = false;
        userRepository.updateUserEmailVerificationStatus(newEmailVerificationStatus, "1a2b3c");
        UserEntity storedUserDetails = userRepository.findByUserId("1a2b3c");
        boolean storedEmailVerificationStatus = storedUserDetails.getEmailVerificationStatus();
        assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);

    }
}