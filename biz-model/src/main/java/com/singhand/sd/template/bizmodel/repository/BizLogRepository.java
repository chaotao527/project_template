package com.singhand.sd.template.bizmodel.repository;
import com.singhand.sd.template.bizmodel.model.BizLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BizLogRepository extends BaseRepository<BizLog, Long> {

  Optional<BizLog> findFirstByTarget_TargetIdAndTarget_TargetTypeOrderByIDDesc(String targetId,
      String targetType);

  Page<BizLog> findByTypeInAndTarget_TargetId(Collection<String> types, String targetId,
                                              Pageable pageable);

  @Query("select b from BizLog b where b.target.targetId = :targetId")
  List<BizLog> findByTarget_TargetId(String targetId);
}