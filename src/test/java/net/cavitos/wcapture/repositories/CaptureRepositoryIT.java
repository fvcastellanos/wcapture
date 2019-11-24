package net.cavitos.wcapture.repositories;

import net.cavitos.wcapture.domain.CaptureHistory;
import net.cavitos.wcapture.fixture.CaptureRepositoryFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        final CaptureHistory expectedCaptureHistory = CaptureRepositoryFixture.buildCaptureHistory();

        captureRepository.save(expectedCaptureHistory);

        final List<CaptureHistory> actualCaptureHistories = getAllCaptureHistories();

        assertThat(actualCaptureHistories.toArray()).isNotEmpty();
        assertThat(actualCaptureHistories.toArray())
                .containsExactly(expectedCaptureHistory);
    }

    @Test
    void testGetCaptureHistory() {

        var captureHistory = CaptureRepositoryFixture.buildCaptureHistory();

        captureRepository.save(captureHistory);

        var request = PageRequest.of(0, 50, Sort.by("id").descending());
        var page = captureRepository.findAll(request);

        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(1L);
        var captures = page.getContent();
        assertThat(captures.toArray()).containsExactly(captureHistory);
    }

    // ------------------------------------------------------------------------------------------------

    private List<CaptureHistory> getAllCaptureHistories() {
        return jdbcTemplate.query("SELECT * FROM capture_history", this::captureHistoryRowMapper);
    }

    private CaptureHistory captureHistoryRowMapper(final ResultSet resultSet, final int rowNumber) throws SQLException {
        return CaptureHistory
                .builder()
                .id(resultSet.getLong("id"))
                .requestId(resultSet.getString("request_id"))
                .storedPath(resultSet.getString("stored_path"))
                .url(resultSet.getString("url"))
                .result(resultSet.getString("result"))
                .build();
    }

}
