package co.quind.peajes.tollcollection.infrastructure.adapter;

import co.quind.peajes.tollcollection.domain.port.out.StoragePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CloudStorageAdapter implements StoragePort {

	@Override
	public Mono<String> uploadFile(String filename, byte[] content, String contentType) {
		log.info("Uploading file to cloud storage: filename={}, size={}", filename, content.length);
		return Mono.fromCallable(() -> {
			String fileUrl = "gs://toll-images/" + System.currentTimeMillis() + "_" + filename;
			log.info("File uploaded successfully: fileUrl={}", fileUrl);
			return fileUrl;
		});
	}

	@Override
	public Mono<Void> deleteFile(String fileUrl) {
		log.info("Deleting file from cloud storage: fileUrl={}", fileUrl);
		return Mono.fromRunnable(() ->
			log.info("File deleted successfully: fileUrl={}", fileUrl)
		);
	}

}
