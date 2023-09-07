package com.singhand.sd.template.bizkeycloakmodel.repository;

import com.singhand.sd.template.bizkeycloakmodel.model.EventEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EventEntityRepository extends JpaRepository<EventEntity, String> {

  @Transactional(readOnly = true)
  Optional<EventEntity> findFirstByUserIdAndTypeIsOrderByEventTimeDesc(String userId, String type);

  @Transactional(readOnly = true)
  @Query("""
      select e.ipAddress, count(e) as c from EventEntity e
      where e.userId = :userId and e.type in ('LOGIN', 'LOGIN_ERROR')
      group by e.ipAddress
      order by c desc
      """)
  List<Object[]> countByIpAddress(String userId);
}