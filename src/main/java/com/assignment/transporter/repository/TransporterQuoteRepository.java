package com.assignment.transporter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.transporter.entity.TransporterQuote;

public interface TransporterQuoteRepository extends JpaRepository<TransporterQuote,Long> {

}
