# Caching Proxy

## Project Overview

The **Caching Proxy Server** is a simple web application that forwards HTTP requests to a specified origin server and caches the responses. If the same request is made again, the cached response is returned instead of forwarding the request to the origin server. This improves performance by reducing load on the origin server for repeated requests.

The project consists of two main components:

- **Proxy Server**: Listens for incoming requests, forwards them to the origin server, and caches the responses.
- **Cache Management**: A method for clearing the cache upon request.

The application uses Spring Boot for the server, and Guava’s `CacheBuilder` is employed for caching responses.

## Environment Requirements

- **Java 1.8 or higher**
- **Maven 3.6 or higher**

## Compilation and Running

1. **Clone the Project Repository**:

   ```bash
   git clone https://github.com/whsad/Caching-Proxy.git
   cd Caching-Proxy
   ```

2. **Compile the Project Using Maven**:

   ```bash
   mvn clean install
   ```

3. **Run the Application**:

   ```bash
   java -jar target/Caching_Proxy-0.1.jar
   ```

4. **Access the Application**:

   Once the application starts, it will be running on the port specified using the `--port` parameter. By default, it runs on **port 3000**.

   - Proxy Server: `http://localhost:3000`

5. **Configure the Origin Server**:

   To specify the origin server (the server to which requests will be forwarded), use the `--origin` parameter. For example:

   ```bash
   caching-proxy --port 8080 --origin http://dummyjson.com
   ```

   This will forward all incoming requests to `http://dummyjson.com` and cache the responses.

6. **Clear the Cache**:

   To clear the cache, run the following command:

   ```bash
   caching-proxy --clear-cache
   ```

   This will remove all cached responses from memory.

## Specific Features

### Proxy Server Behavior

- **Request Forwarding**: When a request is made to the proxy server, it forwards the request to the origin server, retrieves the response, and returns it to the client.

- **Caching**: Responses are cached for 10 minutes. If the same request is made again within that period, the proxy server returns the cached response instead of querying the origin server again.

- Cache Headers

  : The response includes a custom header indicating whether the response was served from the cache or the origin server.

  - **X-Cache: HIT** (if served from cache)
  - **X-Cache: MISS** (if served from the origin server)

### Cache Management

- **Clear Cache**: The cache can be manually cleared by sending a POST request to the `/clear-cache` endpoint. This removes all cached data.

## Project Structure

```
src/main/
├── java/com/kirito
│   ├── controller            # Controller to handle proxy requests and cache clearing
│   ├── CachingProxyApplication.java  # Main application entry point
├── resources
│   └── application.yml       # Configuration file for the proxy server (default port, origin URL)
└── pom.xml                   # Maven project configuration file
```

## Notes

- The cache is implemented using Guava's `CacheBuilder`, which stores the responses in memory for a defined period.
- The `RestTemplate` is used for making HTTP requests to the origin server.
- The application listens on the port specified by the user via the `--port` argument (default: 8080).
- To clear the cache, the `--clear-cache` argument can be used when starting the application.

This is a solution to a project challenge in [roadmap.sh](https://roadmap.sh/projects/caching-server).

