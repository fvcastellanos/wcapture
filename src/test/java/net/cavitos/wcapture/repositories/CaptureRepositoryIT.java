package net.cavitos.wcapture.repositories;

import net.cavitos.wcapture.domain.CaptureHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;

@Transactional
@SpringBootTest
@SpringJUnitConfig
@TestPropertySource({"classpath:application.properties", "classpath:application-test.properties"})
class CaptureRepositoryIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CaptureRepository captureRepository;

    @BeforeEach
    void setUp() {
        deleteFromTables(jdbcTemplate, "capture_history");
    }

    @Test
    void testSave() {
        final CaptureHistory expectedCaptureHistory = CaptureHistory
                .builder()
                .filename("FILENAME-A")
                .url("http://www.fake.com")
                .build();

        captureRepository.save(expectedCaptureHistory);

        final List<CaptureHistory> actualCaptureHistories = getAllCaptureHistories();

        assertThat(actualCaptureHistories.size()).isEqualTo(1);

        final CaptureHistory actualCaptureHistory = actualCaptureHistories.get(0);

        assertThat(actualCaptureHistory)
                               .isEqualToComparingOnlyGivenFields(expectedCaptureHistory, "filename", "url");
    }

    private List<CaptureHistory> getAllCaptureHistories() {
        return jdbcTemplate.query("SELECT * FROM capture_history", this::captureHistoryRowMapper);
    }

    private CaptureHistory captureHistoryRowMapper(final ResultSet resultSet, final int rowNumber) throws SQLException {
        return CaptureHistory
                .builder()
                .id(resultSet.getLong("id"))
                .filename(resultSet.getString("filename"))
                .url(resultSet.getString("url"))
                .build();
    }

}
