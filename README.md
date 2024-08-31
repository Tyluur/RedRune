![RedRune Logo](https://i.imgur.com/uWQTVKc.png)

---


RedRune Server is a Java-based RSPS (RuneScape Private Server) implementation for RuneScape build 666 with OSHD (Old School HD) graphics.
<br>This project aims to provide a robust and feature-rich server emulation for RuneScape enthusiasts.

## Features

- Advanced caching system for efficient game asset management
- Custom networking layer with optimized packet handling
- Sophisticated player and NPC management systems
- Comprehensive world and region implementations
- Robust item and inventory handling
- Detailed combat system with support for various combat styles
- In-depth skill system covering all RuneScape skills
- Quest framework for easy implementation of custom quests
- Advanced data compression and decompression (BZIP2, GZIP)
- Cryptography utilities including Whirlpool hashing and XTEA encryption/decryption
- Custom file systems for efficient data storage and retrieval

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Gradle build tool

### Installation

1. Clone the repository:
   `git clone https://github.com/Tyluur\RedRune.git`
2. Navigate to the project directory:
  ` cd RedRune-Server`
3. Build the project using Gradle:
   `./gradlew build`

### Running the Server

To start the RedRune Server, use the following command: `./gradlew run`

## Contributing

Contributions to RedRune Server are welcome. Please ensure you follow the existing code style and include appropriate tests for new features.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'feat(combat): implement new boss fight mechanics'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

Please use the Conventional Commits specification for your commit messages. Include a scope to provide additional context.

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

## Contact

For any inquiries, please open an issue on the GitHub repository.
