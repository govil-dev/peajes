package co.quind.peajes.tollcollection.domain.port.out;

import reactor.core.publisher.Mono;

public interface StoragePort {
	Mono<String> uploadFile(String filename, byte[] content, String contentType);
	Mono<Void> deleteFile(String fileUrl);
}
