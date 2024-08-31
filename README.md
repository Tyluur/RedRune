![RedRune Logo](https://i.imgur.com/uWQTVKc.png)

---

RedRune Server is a Java-based RSPS (RuneScape Private Server) implementation for RuneScape build 667 with OSHD (Old School HD) graphics.
<br>This project aims to provide a robust and feature-rich server emulation for RuneScape enthusiasts.

## Features

- Support for RuneScape build 667
- OSHD (Old School HD) graphics compatibility
- Sophisticated caching system for game assets and data
- Custom input/output stream implementations
- Support for multiple compression algorithms (BZIP2, GZIP)
- Cryptography utilities including Whirlpool hashing and XTEA decryption
- Indexed color image handling

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Gradle build tool

### Installation

1. Clone the repository: `git clone https://github.com/yourusername/RedRune-Server.git`
2. Navigate to the project directory:   `cd RedRune-Server`
3. Build the project using Gradle:   `./gradlew build`

### Running the Server

To start the RedRune Server, use the following command: `./gradlew run`

## Project Structure

- com.alex.io: Input/output handling
- com.alex.loaders: Data loading utilities
- com.alex.store: Data storage and caching
- com.alex.util: Utility classes including compression algorithms
- org.redrune: Main server package
- org.redrune.cache: Cache management

## Contributing

Contributions to RedRune Server are welcome. Please ensure you follow the existing code style and include appropriate tests for new features.

1. Fork the repository
2. Create your feature branch (git checkout -b feature/AmazingFeature)
3. Commit your changes (git commit -m 'Add some AmazingFeature')
4. Push to the branch (git push origin feature/AmazingFeature)
5. Open a Pull Request

## License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](LICENSE) file for details.

## Acknowledgements

- RuneScape by Jagex Ltd.
- BZIP2 Compression
- GZIP Compression
- Whirlpool Hashing
- XTEA Encryption

## Disclaimer

This project is not affiliated with or endorsed by Jagex Ltd. It is an independent, educational endeavor aimed at understanding and recreating game server technologies.
