package com.cg.creditcardpayment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.creditcardpayment.entity.LoginEntity;

@Repository
public interface ILoginRepository extends JpaRepository<LoginEntity, String>{
	
}
