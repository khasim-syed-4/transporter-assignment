package com.assignment.transporter.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.assignment.transporter.entity.Lane;

public interface LaneRepository extends JpaRepository<Lane,Long>{

}
