default: clean build run

clean:
	@rm build/libs/*.jar

.PHONY: build
build:
	@./gradlew bootJar
	@podman build --no-cache -t localhost/whattocook:dev .

run:
	@podman-compose -f docker-compose.dev.yml up
