package com.singhand.sd.template.bizmodel.repository;

import com.singhand.sd.template.bizmodel.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
}