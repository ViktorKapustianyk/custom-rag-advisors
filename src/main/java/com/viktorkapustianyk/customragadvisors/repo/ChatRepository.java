package com.viktorkapustianyk.customragadvisors.repo;

import com.viktorkapustianyk.customragadvisors.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
}
