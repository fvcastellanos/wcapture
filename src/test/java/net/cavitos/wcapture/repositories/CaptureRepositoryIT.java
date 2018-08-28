package net.cavitos.wcapture.repositories;

import net.cavitos.wcapture.domain.CaptureHistory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@SpringBootTest
public class CaptureRepositoryIT extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CaptureRepository captureRepository;

    @Before
    public void setUp() {
        deleteFromTables("capture_history");
    }

    @Test
    public void testInsert() {
        final CaptureHistory captureHistory = CaptureHistory
                .builder()
                .filename("FILENAME-A")
                .url("http://www.fake.com")
                .build();

        captureRepository.insert(captureHistory);

        final List<CaptureHistory> expectedCaptureHistories = getAllCaptureHistories();

        assertThat(expectedCaptureHistories.size(), is(1));

        final CaptureHistory expectedCaptureHistory = expectedCaptureHistories.get(0);

        assertThat(expectedCaptureHistory.getId(), is(captureHistory.getId()));
        assertThat(expectedCaptureHistory.getCreatedDate(), is(not(nullValue())));
        assertThat(expectedCaptureHistory.getModifiedDate(), is(not(nullValue())));
        assertThat(expectedCaptureHistory.getFilename(), is(captureHistory.getFilename()));
        assertThat(expectedCaptureHistory.getUrl(), is(captureHistory.getUrl()));
    }

    private List<CaptureHistory> getAllCaptureHistories() {
        return jdbcTemplate.query("SELECT * FROM capture_history", this::captureHistoryRowMapper);
    }

    private CaptureHistory captureHistoryRowMapper(final ResultSet resultSet, final int rowNumber) throws SQLException {
        return CaptureHistory
                .builder()
                .id(resultSet.getLong("id"))
                .createdDate(resultSet.getTimestamp("created_date").toInstant())
                .modifiedDate(resultSet.getTimestamp("modified_date").toInstant())
                .filename(resultSet.getString("filename"))
                .url(resultSet.getString("url"))
                .build();
    }

}
