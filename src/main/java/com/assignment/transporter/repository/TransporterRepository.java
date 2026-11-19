package com.assignment.transporter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.transporter.entity.Transporter;

public interface TransporterRepository extends JpaRepository<Transporter,Long> {

}
