package com.tkis.qedbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tkis.qedbot.entity.UserProjectMapping;

public interface UserProjectMappingRepo  extends JpaRepository<UserProjectMapping, Integer>{

}
