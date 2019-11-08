package net.cavitos.wcapture.message.producer;

import net.cavitos.wcapture.client.model.CaptureRequest;

public interface CaptureRequestProducer {

    void produce(CaptureRequest request);
}
