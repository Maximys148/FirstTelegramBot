package org.example.bot.repository;

import org.example.bot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, String>{

}
