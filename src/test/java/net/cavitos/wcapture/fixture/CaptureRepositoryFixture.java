package net.cavitos.wcapture.fixture;

import net.cavitos.wcapture.domain.CaptureHistory;

public class CaptureRepositoryFixture {

    private CaptureRepositoryFixture() {
    }

    public static CaptureHistory buildCaptureHistory() {

        return CaptureHistory
                .builder()
                .requestId("1234")
                .result("OK")
                .storedPath("https://cdn.net/image.jpg")
                .url("http://www.fake.com")
                .build();
    }
}
