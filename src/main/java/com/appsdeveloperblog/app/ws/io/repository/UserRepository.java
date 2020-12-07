package com.appsdeveloperblog.app.ws.io.repository;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    //<1,2> 1 the class that needs to be persisted. 2 id,  long
    UserEntity findByEmail(String email); // findBy + *
    UserEntity findByUserId(String userId);


    // 从数据库查，数据库字段
    // findAllUSersWithConfirmedEmailAddress 函数名无所谓
    // countQuery 为了分页
    @Query(value = "select * from users u where u.email_verification_status = '1'",
            countQuery = "select count(*) from users u where u.email_verification_status = '1'",
            nativeQuery = true)
    Page<UserEntity> findAllUSersWithConfirmedEmailAddress(Pageable pageableRequest);

    @Query(value = "select * from users u where u.first_name = ?1",
        nativeQuery = true)
    List<UserEntity> findUserByFirstName(String firstName);

    @Query(value = "select * from users u where u.last_name = :lastName",
            nativeQuery = true)
    List<UserEntity> findUserByLastName(@Param("lastName") String lastName);

    // %:keyword, (has to end with keyword),
    @Query(value = "select * from users u where u.last_name LIKE %:keyword% or u.first_name LIKE %:keyword%",
            nativeQuery = true)
    List<UserEntity> findUsersByKeyword(@Param("keyword") String keyword);

    @Query(value = "select u.first_name, u.last_name from users u where u.last_name LIKE %:keyword% or u.first_name LIKE %:keyword%",
            nativeQuery = true)
    List<Object[]> findUserFirstNameAndLastNameByKeyword(@Param("keyword") String keyword);

    @Transactional
    @Modifying
    @Query(value="update users u set u.email_verification_status=:emailVerificationStatus where u.user_id=:userId", nativeQuery = true)
    void updateUserEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus,
                                           @Param("userId") String userId);


    @Query("select user from UserEntity user where user.userId = :userId")
    UserEntity findUserEntityByUserId(@Param("userId") String userId);//函数名无所谓

    @Query("select user.firstName, user.lastName from UserEntity user where user.userId = :userId")
    List<Object[]> getUserEntityFullNameByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("update UserEntity u set u.emailVerificationStatus =:emailVerificationStatus where u.userId = :userId")
    void updateUserEntityEmailVerificationStatus(
            @Param("emailVerificationStatus") boolean emailVerificationStatus,
            @Param("userId") String userId
    );

}
