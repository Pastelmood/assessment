package com.kbtg.bootcamp.posttest.repository;

import com.kbtg.bootcamp.posttest.entity.UserTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTicketRepository extends JpaRepository<UserTicket, Integer> {

    List<UserTicket> findByUserId(int userId);

}

//@Query("select u from User u where u.emailAddress = ?1")
//User findByEmailAddress(String emailAddress);

//@Query("select p from Person AS p"
//        + " ,Name AS n"
//        + " where p.forename = n.forename "
//        + " and p.surname = n.surname"
//        + " and n = :name")
//Set<Person>findByName(@Param("name") Name name);