package com.cmpe275.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmpe275.entity.User;
import java.util.Optional;

@Transactional
@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	
    
    
    public Optional<User> findByUsername(String parameter);

	public Optional<User> findByNickname(String parameter);

	public Optional<User> getById(long id);

	public Optional<User> getByUsername(String username);

	public User findByUsernameAndSignupType(String name, String type);

	
    
	
}
