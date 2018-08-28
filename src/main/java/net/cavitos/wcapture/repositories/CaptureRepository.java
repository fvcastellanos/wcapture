package net.cavitos.wcapture.repositories;

import net.cavitos.wcapture.domain.CaptureHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

import static org.apache.ibatis.annotations.Options.FlushCachePolicy.TRUE;

@Repository
public interface CaptureRepository {

    @Insert("INSERT INTO capture_history (url, filename) VALUES (#{url}, #{filename})")
    @Options(useGeneratedKeys = true, keyColumn = "id", flushCache = TRUE)
    void insert(CaptureHistory captureHistory);

}
