package net.cavitos.wcapture.repositories;

import net.cavitos.wcapture.domain.CaptureHistory;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.apache.ibatis.annotations.Options.FlushCachePolicy.TRUE;

@Repository
@CacheNamespace(flushInterval = 86400000L, readWrite = false)
public interface CaptureHistoryRepository {

    @Insert("INSERT INTO capture_history (url, filename) VALUES (#{url}, #{filename})")
    @Options(useGeneratedKeys = true, keyColumn = "id", flushCache = TRUE)
    void insert(CaptureHistory captureHistory);

    @Select("SELECT * FROM capture_history")
    @ResultMap("net.cavitos.wcapture.mapper.mybatis.captureHistoryMap")
    List<CaptureHistory> findAll();

}
