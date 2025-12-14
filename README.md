# Personal JSON Database

A lightweight client/server key–value store implemented in Java 17. The server keeps
all data inside a JSON document on disk and exposes a socket protocol that accepts
`set`, `get`, `delete`, and `exit` commands. A small CLI client is bundled to send
requests either from command‑line flags or JSON files.

## Highlights
- Multi-threaded TCP server (`Server` + `ClientHandler`) running on port `23456`.
- Nested keys: pass a JSON array such as `["profile","location","city"]` to reach
  deep nodes inside the document.
- Concurrent persistence: read/write locks protect `JsonDatabase`, and every
  successful mutation is flushed back to `src/main/java/app/hyperskill/server/data/database.json`.
- Gson handles request/response serialization, JCommander powers the client CLI.

## Project Layout
```
pom.xml                               # Maven build file
src/main/java/app/hyperskill/client   # CLI arguments parser + TCP client
src/main/java/app/hyperskill/server   # TCP server, command registry, JSON DB
src/main/java/app/hyperskill/server/data/database.json  # On-disk storage
```

## Prerequisites
- JDK 17+
- Maven 3.8+

## Build
```bash
mvn -q -DskipTests package
```
The command compiles the sources and places classes under `target/classes` and a
(non-fat) JAR inside `target/`.

## Running the server
In one terminal:
```bash
mvn -q org.codehaus.mojo:exec-maven-plugin:3.1.0:java \
    -Dexec.mainClass=app.hyperskill.server.Main
```
The exec plugin is downloaded on demand; no extra configuration is required. The
server prints `Server started!` and begins accepting connections.

## Sending requests via the client
Use a second terminal. The client understands either long or short flags
(`--type`/`-t`, `--key`/`-k`, `--value`/`-v`) as well as the `-in` flag to read a
JSON request from `src/main/java/app/hyperskill/client/data/`.

### Command examples
```bash
# Set a scalar value
env REQUEST='-t set -k name -v "Hyperskill"'
mvn -q org.codehaus.mojo:exec-maven-plugin:3.1.0:java \
    -Dexec.mainClass=app.hyperskill.client.Main \
    -Dexec.args="$REQUEST"

# Read a nested value (note the JSON array)
mvn -q org.codehaus.mojo:exec-maven-plugin:3.1.0:java \
    -Dexec.mainClass=app.hyperskill.client.Main \
    -Dexec.args='-t get -k ["profile","location","city"]'

# Delete using a JSON file stored under client/data/
mvn -q org.codehaus.mojo:exec-maven-plugin:3.1.0:java \
    -Dexec.mainClass=app.hyperskill.client.Main \
    -Dexec.args='-in request.json'
```

### Request and response schema
Either of these payload shapes is accepted:
```json
{"t":"set","k":"name","v":"Ada"}
{"type":"get","key":["profile","location"],"value":null}
```
Responses follow this pattern:
```json
{"response":"OK"}
{"response":"OK","value":"Ada"}
{"response":"ERROR","reason":"No such key"}
```
Use `exit` to tell the server to persist any pending changes and shut down.

## Persistence notes
- The JSON file is created automatically on first run.
- Keys mapped to complex structures remain valid JSON—arrays and objects are stored
  verbatim.
- Because every write hits disk immediately, you can inspect
  `src/main/java/app/hyperskill/server/data/database.json` while the server is running.

## Testing
Run the (placeholder) unit suite with:
```bash
mvn test
```
Add additional tests under `src/test/java` as you extend the database.

## Troubleshooting
- "Address already in use" → stop existing server processes before relaunching.
- "Unknown command" → only `set`, `get`, `delete`, and `exit` are registered inside
  `CommandRegistry`.
- Empty responses usually mean the request JSON was malformed. Use the client `-in`
  option to keep complex payloads in version-controlled files.
