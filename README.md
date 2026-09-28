<div align="center">

# ⚡ StreamCore

**High-Performance Distributed OTT & HLS Media Streaming Engine**

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![FFmpeg](https://img.shields.io/badge/FFmpeg-HLS_Transcoding-007808?style=for-the-badge&logo=ffmpeg&logoColor=white)](https://ffmpeg.org/)
[![AWS S3 / B2](https://img.shields.io/badge/Storage-Backblaze_B2-FF3E00?style=for-the-badge&logo=amazon-s3&logoColor=white)](https://www.backblaze.com/b2/)
[![Security](https://img.shields.io/badge/Auth-JWT_Stateless-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)](https://jwt.io/)
[![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](LICENSE)

<p align="center">
  <a href="#-key-features">Key Features</a> •
  <a href="#-system-architecture">System Architecture</a> •
  <a href="#-tech-stack">Tech Stack</a> •
  <a href="#-api-reference">API Reference</a> •
  <a href="#-getting-started">Getting Started</a> •
  <a href="#-configuration">Configuration</a>
</p>

</div>

---

## 📌 Overview

**StreamCore** is an enterprise-grade backend engine designed for video-on-demand (VOD) streaming. It eliminates media ingestion bottlenecks by decoupling video uploads using cloud-native pre-signed URLs, dynamically processing high-definition video through an asynchronous multi-threaded FFmpeg pipeline into adaptive HLS (`.m3u8` / `.ts`) streams, and orchestrating subscription billing alongside real-time watch telemetry.

---

## 🚀 Key Features

* **Direct-to-Cloud Ingestion**: Generates short-lived AWS S3/Backblaze B2 Pre-signed URLs for zero-proxy client-to-bucket uploads, preventing application memory exhaustion.
* **Async HLS Transcoding Pipeline**: Offloads video processing to a dedicated bounded thread pool (`transcoderTaskExecutor`), segmenting raw `.mp4` into 6-second `.ts` chunks with fixed GOP boundaries and generating master playlists.
* **Storage Footprint Optimization**: Automates post-processing garbage collection by destroying multi-gigabyte raw video files from storage once chunking is finalized.
* **Low-Latency Edge Delivery**: Distributes VOD manifests via Cloudflare CDN integration for instant bufferless video streaming.
* **Fine-Grained Monetization Engine**: Dual-revenue engine supporting tiered recurring subscriptions (with resolution and concurrent screen enforcement) and pay-per-view video rentals.
* **Telemetry & Watch Resume**: Captures periodic viewer pings to track progress, compute exact completion metrics (auto-completion at >90%), and resume playback seamlessly.
* **Stateless Security Architecture**: Custom JWT filter pipeline with BCrypt hashing and role-based access control (RBAC).

---

## 🏗️ System Architecture

```text
  [ Client Application ]
        │
        │ 1. Request Upload URL
        ▼
   [ StreamCore API ] ────► 2. Issue S3 Presigned URL (15-min TTL)
        │
        │ 3. Direct Upload Raw .mp4 (Bypasses API Gateway)
        ▼
   [ Backblaze B2 Storage ]
        │
        │ 4. Transcode Request Triggered
        ▼
┌────────────────────────────────────────────────────────┐
│             FFmpeg Transcoding Engine                  │
│  - Downloads raw video to isolated sandbox             │
│  - Normalizes to 24fps & fixed GOP intervals (144 frames)
│  - Slices video into 6-sec .ts segments & .m3u8 index  │
│  - Uploads generated chunks to `streams/{id}/`         │
│  - Purges bulky raw source video from cloud bucket     │
└────────────────────────────────────────────────────────┘
        │
        │ 5. Request Master Stream
        ▼
 [ Cloudflare CDN Edge ] ◄─── Serves HLS Segments directly to Player
```

---

## 🛠️ Tech Stack

| Layer | Technologies |
| :--- | :--- |
| **Runtime & Core** | Java 21, Spring Boot 3, Spring Web |
| **Media Processing** | FFmpeg (libx264, AAC audio, HLS multiplexer) |
| **Database & ORM** | PostgreSQL / MySQL, Spring Data JPA, Hibernate |
| **Cloud Storage** | Backblaze B2, AWS S3 SDK v2 (`software.amazon.awssdk`) |
| **Security & Auth** | Spring Security 6, JJWT (`io.jsonwebtoken`), BCrypt |
| **Data Flow & Tools** | Java Records, Lombok, Jakarta Bean Validation |

---

## 📡 API Reference

### 🔐 Authentication

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :---: |
| `POST` | `/auth/register` | Register a new user | ❌ |
| `POST` | `/auth/login` | Authenticate and obtain JWT token | ❌ |

<details>
<summary>▶ View Register & Login Payloads</summary>

**`POST /auth/register`**
```json
{
  "username": "stream_user",
  "email": "user@example.com",
  "password": "StrongPassword123!",
  "profilePicture": "https://img.cdn.com/avatar.png"
}
```

**`POST /auth/login`**
```json
{
  "identifier": "stream_user",
  "password": "StrongPassword123!"
}
```
</details>

---

### 🎬 Media & Transcoding

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :---: |
| `GET` | `/api/media/upload-url?movieId={id}` | Get pre-signed S3 upload URL | ✅ |
| `POST` | `/api/media/transcode/start/{movieId}` | Queue asynchronous HLS transcoding | ✅ |
| `GET` | `/api/media/play/{movieId}` | Retrieve CDN playback manifest URL | ✅ |

---

### 🎥 Movie Catalog

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :---: |
| `POST` | `/api/movies` | Register movie record in metadata store | ✅ |
| `GET` | `/api/movies/{id}` | Get movie details by ID | ✅ |
| `GET` | `/api/movies` | List all available movies | ✅ |
| `DELETE`| `/api/movies/{id}` | Delete a movie | ✅ |

---

### 💳 Plans, Subscriptions & Telemetry

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :---: |
| `POST` | `/premium_plan/save` | Create a new subscription tier | ✅ |
| `GET` | `/premium_plan/{id}` | Get plan details | ✅ |
| `POST` | `/api/subscriptions/purchase/{planId}`| Subscribe via payment transaction | ✅ |
| `POST` | `/api/history/ping` | Send real-time watch telemetry ping | ✅ |

---

## ⚙️ Configuration

Set up your `src/main/resources/application.properties` (or supply environment variables):

```properties
# Server
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/streamcore_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# Cloud Storage (Backblaze B2 / AWS S3)
b2.endpoint=https://s3.us-east-005.backblazeb2.com
b2.region=us-east-005
b2.accessKeyId=${B2_ACCESS_KEY_ID}
b2.secretAccessKey=${B2_SECRET_ACCESS_KEY}
b2.bucketName=streamcore-media-bucket

# CDN Routing
cdn.domain=https://media-cdn.yourdomain.com

# Security / JWT (Min 256-bit key)
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000
```

---

## 🚦 Getting Started

### Prerequisites

* **JDK 21+** installed
* **FFmpeg** installed and accessible in system `PATH`
  ```bash
  # Linux
  sudo apt install ffmpeg -y

  # macOS
  brew install ffmpeg
  ```
* **PostgreSQL** running locally or via Docker
* A **Backblaze B2** (or AWS S3) bucket with read/write access

### Installation & Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Mohit9-y/StreamCore.git
   cd StreamCore
   ```

2. **Build and install dependencies:**
   ```bash
   ./mvnw clean install
   ```

3. **Launch the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

---

## 🧪 Testing the Streaming Pipeline

1. **Create a Movie**: `POST /api/movies` to acquire a `movieId`.
2. **Fetch Presigned Upload URL**: Call `GET /api/media/upload-url?movieId=<UUID>`.
3. **Upload Raw Video**: Send an HTTP `PUT` request with your `.mp4` file directly to the returned `uploadUrl`.
4. **Trigger Transcoder**: Call `POST /api/media/transcode/start/<UUID>`. StreamCore will pull, segment, write playlist indexes, and push HLS chunks back to cloud storage.
5. **Stream**: Request `GET /api/media/play/<UUID>` and feed the resulting `manifestUrl` into any HLS player (e.g., [HLS.js](https://hls-js.netlify.app/demo/) or Safari).

---
