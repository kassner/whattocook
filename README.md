# What to cook?

You don't know what to cook today? This project will suggest you one recipe, adapting to the ingredients that you have available at home.

## Running

Running with Docker Compose:

```yaml
# File: docker-compose.yaml
version: '3.9'

services:
  app:
    image: ghcr.io/kassner/whattocook:0.1.0
    ports:
      - "127.0.0.1:12393:8080"
    environment:
      SPRING_DATASOURCE_URL: "jdbc:postgresql://postgres:5432/whattocook"
      SPRING_DATASOURCE_USERNAME: whattocook
      SPRING_DATASOURCE_PASSWORD: admin123

  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: whattocook
      POSTGRES_USER: whattocook
      POSTGRES_PASSWORD: admin123
```
