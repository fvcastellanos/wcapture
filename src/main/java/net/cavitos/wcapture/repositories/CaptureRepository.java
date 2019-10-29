package net.cavitos.wcapture.repositories;

import net.cavitos.wcapture.domain.CaptureHistory;
import org.springframework.data.mybatis.repository.MybatisRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptureRepository extends MybatisRepository<CaptureHistory, Long> {

}
