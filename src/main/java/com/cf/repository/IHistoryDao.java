package com.cf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cf.model.History;

@Repository
public interface IHistoryDao extends JpaRepository<History, Integer> {

}
