package com.triple.triplehomework.repository;

import com.triple.triplehomework.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserUuid(String userUuid);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.point=:point where u.userId=:userId")
    void updatePoint(@Param("userId") Long userId, @Param("point") int point);
}
