# 22200543 이어진

## 웹 서비스 개발 01분반 - 5주차 과제
### 프로젝트 주제: 동아리 활동 관리 REST API

---

# ① 프로젝트 소개

## 주제와 관리하는 데이터

Spring Boot를 이용하여 **동아리 활동 관리 REST API**를 구현하였다. 동아리 활동의 제목, 동아리명, 카테고리, 활동 날짜, 장소, 참여 인원, 설명을 관리하며 등록·전체 조회·단건 조회·수정·삭제 기능을 제공한다.

데이터베이스 대신 Java Collection 기반의 메모리 저장소를 사용하였다.

| 필드 | 타입 | 설명 |
|---|---|---|
| `id` | `Long` | 활동 고유 ID, 등록 시 자동 생성 |
| `title` | `String` | 활동 제목 |
| `clubName` | `String` | 동아리명 |
| `category` | `String` | 활동 카테고리 |
| `activityDate` | `String` | 활동 날짜 |
| `location` | `String` | 활동 장소 |
| `participants` | `Integer` | 참여 인원 |
| `description` | `String` | 활동 설명 |

기본 CRUD 외에 잘못된 입력에 대한 `400 Bad Request` 처리와 `category`를 이용한 필터링 기능을 추가하였다.

## 프로젝트 구조

```text
spring_crud_activity
├── Dockerfile
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── README.md
├── test.http
└── src/main
    ├── java/org/example/spring_crud_activity
    │   ├── SpringCrudActivityApplication.java
    │   ├── controller/ClubActivityController.java
    │   ├── service/ClubActivityService.java
    │   ├── repository
    │   │   ├── ClubActivityRepository.java
    │   │   └── ClubActivityMemoryRepository.java
    │   ├── domain/ClubActivity.java
    │   ├── dto
    │   │   ├── ClubActivityRequest.java
    │   │   └── ClubActivityResponse.java
    │   └── exception
    │       ├── ActivityNotFoundException.java
    │       └── GlobalExceptionHandler.java
    └── resources/application.properties
```

요청 처리 구조:

```text
ClubActivityController
        ↓
ClubActivityService
        ↓
ClubActivityRepository
        ↓
ClubActivityMemoryRepository
        ↓
ConcurrentHashMap<Long, ClubActivity>
```

## 로컬 실행 방법

1. IntelliJ IDEA에서 프로젝트를 연다.
2. Project SDK와 Gradle JVM을 JDK 23으로 설정한다.
3. `SpringCrudActivityApplication.java`를 실행한다.
4. `http://localhost:8080`에서 API를 사용한다.
5. `test.http`를 이용하여 IntelliJ HTTP Client에서 요청을 테스트한다.

Windows PowerShell:

```powershell
.\gradlew bootRun
```

## API Endpoint

| Method | URL | 기능 |
|---|---|---|
| `POST` | `/api/activities` | 활동 등록 |
| `GET` | `/api/activities` | 전체 활동 조회 |
| `GET` | `/api/activities/{id}` | 특정 활동 조회 |
| `PUT` | `/api/activities/{id}` | 활동 수정 |
| `DELETE` | `/api/activities/{id}` | 활동 삭제 |
| `GET` | `/api/activities?category={category}` | 카테고리별 조회 |

## 요청·응답 JSON 예시

Request:

```json
{
  "title": "신입 부원 환영회",
  "clubName": "한동 러닝 동아리",
  "category": "친목",
  "activityDate": "2026-10-10",
  "location": "한동대학교 운동장",
  "participants": 15,
  "description": "신입 부원들과 함께 러닝 및 친목 활동"
}
```

Response:

```json
{
  "id": 1,
  "title": "신입 부원 환영회",
  "clubName": "한동 러닝 동아리",
  "category": "친목",
  "activityDate": "2026-10-10",
  "location": "한동대학교 운동장",
  "participants": 15,
  "description": "신입 부원들과 함께 러닝 및 친목 활동"
}
```

## GitHub Repository 및 배포 URL

- GitHub Repository: https://github.com/eojinleecode/sb_restapi
- 배포 URL: https://sb-restapi-2.onrender.com

---

# ② 개발환경 및 Dependency

| 항목 | 작성 내용 |
|---|---|
| IDE | IntelliJ IDEA |
| JDK | Java 23 |
| Spring Boot | 4.1.1 |
| Build Tool | Gradle 9.7.1 |
| 데이터 저장 | `ConcurrentHashMap<Long, ClubActivity>` |
| 테스트 | IntelliJ HTTP Client (`test.http`) |
| 배포 환경 | Render + Docker |
| 배포 URL | `https://sb-restapi-2.onrender.com` |

## Dependency

### Spring Boot Starter WebMVC

```gradle
implementation 'org.springframework.boot:spring-boot-starter-webmvc'
```

REST API를 구현하고 `@RestController`, `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` 등을 사용하기 위해 필요하다.

### Spring Boot Starter WebMVC Test

```gradle
testImplementation 'org.springframework.boot:spring-boot-starter-webmvc-test'
```

Spring MVC 기반 애플리케이션의 테스트 기능을 사용하기 위해 포함하였다.

### JUnit Platform Launcher

```gradle
testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
```

Gradle에서 JUnit 기반 테스트를 실행하기 위한 테스트 실행 환경이다.

---

# ③ Solution 분석

제공된 Book CRUD Solution을 참고하여 Controller → Service → Repository → Memory의 흐름과 DTO, ID 생성, 404 처리, Response DTO 변환 과정을 분석하였다.

## Q1. Controller → Service → Repository → Memory의 요청 처리 흐름은 어떻게 되는가?

Controller가 HTTP 요청을 받고 Service를 호출한다. Service는 비즈니스 로직을 수행하고 Repository Interface를 통해 데이터 처리를 요청한다. 실제 데이터 저장 및 조회는 Memory Repository가 Java Collection을 이용하여 수행한다.

현재 프로젝트의 관련 클래스 및 메서드는 `ClubActivityController.create()`, `ClubActivityService.create()`, `ClubActivityMemoryRepository.save()`이다.

## Q2. Request DTO, Domain 객체, Response DTO의 역할은 무엇인가?

`ClubActivityRequest`는 등록·수정 요청 데이터를 전달하고, `ClubActivity`는 프로그램 내부의 Domain 객체이며, `ClubActivityResponse`는 클라이언트에게 반환할 응답 데이터를 담당한다.

관련 클래스 및 메서드는 `ClubActivityRequest`, `ClubActivity`, `ClubActivityResponse`, `ClubActivityService.create()`, `ClubActivityService.toResponse()`이다.

## Q3. 새로운 데이터의 ID는 어디에서 생성되는가?

`ClubActivityMemoryRepository.save()`에서 새로운 ID를 생성하여 Domain 객체에 저장한다. 따라서 사용자는 Request에 ID를 직접 입력하지 않아도 된다.

관련 클래스 및 메서드는 `ClubActivityMemoryRepository`, `save()`이다.

## Q4. 존재하지 않는 ID에 대해 어떻게 404가 반환되는가?

`ClubActivityService.findById()`에서 해당 ID의 데이터가 없으면 `ActivityNotFoundException`을 발생시킨다. `GlobalExceptionHandler`가 이를 처리하여 `404 Not Found`를 반환한다. 수정과 삭제에서도 존재하지 않는 ID를 같은 방식으로 처리한다.

관련 클래스 및 메서드는 `ClubActivityService.findById()`, `update()`, `delete()`, `ActivityNotFoundException`, `GlobalExceptionHandler`이다.

## Q5. Domain 객체는 어떻게 Response DTO로 변환되는가?

`ClubActivityService.toResponse()`에서 `ClubActivity`의 값을 이용하여 `ClubActivityResponse`를 생성한다. Domain 객체를 그대로 반환하지 않고 API 응답용 DTO로 변환한다.

관련 클래스 및 메서드는 `ClubActivityService.toResponse()`, `ClubActivityResponse`이다.

## Q6. Repository Interface와 Memory Repository를 분리한 이유는 무엇인가?

`ClubActivityRepository`는 저장·조회·수정·삭제 기능을 정의하고 `ClubActivityMemoryRepository`가 이를 구현한다. Service가 구체적인 저장 방식이 아니라 Repository Interface를 통해 데이터를 처리할 수 있도록 역할을 분리하였다.

---

# ④ 개발 과정 요약

## 1단계. 프로젝트 생성 및 환경 설정

IntelliJ의 Spring Boot New Project를 이용하여 새 프로젝트를 생성하였다. Java 23, Gradle, Spring WebMVC를 사용하도록 설정하고 애플리케이션을 실행하여 서버가 정상적으로 시작되는지 확인하였다.

## 2단계. Domain 및 DTO 작성

동아리 활동을 나타내는 `ClubActivity`를 작성하고 요청과 응답을 분리하기 위해 `ClubActivityRequest`, `ClubActivityResponse`를 작성하였다.

## 3단계. Repository 및 Service 구현

`ClubActivityRepository` Interface와 `ClubActivityMemoryRepository`를 작성하였다. Memory Repository에서는 `ConcurrentHashMap`을 이용해 데이터를 저장하였다. `ClubActivityService`에서 CRUD 로직과 Response DTO 변환을 구현하였다.

## 4단계. Controller 및 예외 처리 구현

`ClubActivityController`에서 POST, GET, PUT, DELETE API를 구현하였다. 존재하지 않는 ID는 `ActivityNotFoundException`과 `GlobalExceptionHandler`를 이용하여 404로 처리하였다.

## 5단계. 기능 확장·테스트·배포

잘못된 입력 처리와 카테고리 검색을 추가하였다. `test.http`로 CRUD, 400, 404, 카테고리 필터링을 테스트한 후 Dockerfile을 작성하고 Render에 배포하였다. 배포 URL에서도 GET, POST와 확장 기능을 테스트하였다.

---

# ⑤ 기능 수정·확장

## A. 잘못된 입력 처리

### 기능을 추가한 이유

필수 데이터가 없거나 잘못된 참여 인원이 저장되는 것을 방지하기 위해 입력 검증 기능을 추가하였다.

검사 조건:
- 활동 제목이 비어 있으면 안 됨
- 활동 날짜가 비어 있으면 안 됨
- 참여 인원이 1명 미만이면 안 됨

### 수정한 클래스와 메서드

- `ClubActivityService.validate()`
- `ClubActivityService.create()`
- `ClubActivityService.update()`
- `GlobalExceptionHandler`

### 정상 입력 테스트

```http
POST https://sb-restapi-2.onrender.com/api/activities
Content-Type: application/json

{
  "title": "가을 정기 러닝",
  "clubName": "한동 러닝 동아리",
  "category": "운동",
  "activityDate": "2026-10-15",
  "location": "포항 영일대",
  "participants": 12,
  "description": "가을 정기 러닝 활동"
}
```

예상 결과: 정상 등록  
실제 결과: 정상적으로 활동이 등록되는 것을 확인하였다.

### 잘못된 입력 테스트

```http
POST https://sb-restapi-2.onrender.com/api/activities
Content-Type: application/json

{
  "title": "",
  "clubName": "한동 러닝 동아리",
  "category": "친목",
  "activityDate": "2026-10-10",
  "location": "한동대학교 운동장",
  "participants": 0,
  "description": "잘못된 입력 테스트"
}
```

예상 결과: `400 Bad Request`  
실제 결과: `400 Bad Request`가 반환되는 것을 확인하였다.

## B. 카테고리별 조회 기능

### 기능을 추가한 이유

전체 활동뿐 아니라 원하는 종류의 활동만 확인할 수 있도록 `category` 필터링 기능을 추가하였다.

### 수정한 클래스와 메서드

- `ClubActivityController.findAll()`
- `ClubActivityService.findByCategory()`
- `ClubActivityRepository.findByCategory()`
- `ClubActivityMemoryRepository.findByCategory()`

### 테스트

```http
GET https://sb-restapi-2.onrender.com/api/activities?category=친목
```

예상 결과: `친목` 카테고리 활동만 반환  
실제 결과: `친목` 카테고리에 해당하는 활동만 반환되는 것을 확인하였다.

```http
GET https://sb-restapi-2.onrender.com/api/activities?category=봉사
```

예상 결과: `[]`  
실제 결과: 조건에 맞는 데이터가 없어 빈 배열이 반환되는 것을 확인하였다.

---

# ⑥ 배포 과정 요약

## 빌드 및 배포 순서

1. IntelliJ에서 REST CRUD 구현 및 로컬 테스트
2. 프로젝트 루트에 `Dockerfile` 추가
3. `application.properties`에 배포 포트 설정
4. 개인 GitHub Repository `sb_restapi`에 Push
5. Render에서 GitHub Repository 연결
6. Docker 방식으로 빌드 및 배포
7. 실제 배포 URL에서 API 테스트

## 추가·수정한 파일

### Dockerfile

```dockerfile
FROM eclipse-temurin:23-jdk

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar --no-daemon

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "build/libs/spring_crud_activity-0.0.1-SNAPSHOT.jar"]
```

### application.properties

```properties
server.port=${PORT:8080}
```

## 배포 중 발생한 문제와 해결 방법

처음 Render에서 배포할 때 다음 오류가 발생하였다.

```text
failed to read dockerfile: open Dockerfile: no such file or directory
```

원인은 Dockerfile이 프로젝트 루트에 존재하지 않았기 때문이다. Dockerfile을 `build.gradle`, `settings.gradle`, `gradlew`와 같은 프로젝트 루트에 생성하고 GitHub에 Push한 후 재배포하여 해결하였다.

배포 후 IntelliJ HTTP Client에서 `localhost:8080`으로 요청하여 `Connection refused` 오류도 발생하였다. 배포 테스트의 요청 주소를 실제 Render URL인 `https://sb-restapi-2.onrender.com`으로 변경하여 해결하였다.

## 배포 URL 테스트

배포 URL:

```text
https://sb-restapi-2.onrender.com
```

전체 조회:

```http
GET https://sb-restapi-2.onrender.com/api/activities
```

배포 직후 메모리 저장소가 비어 있어 `[]`가 반환되는 것을 확인하였다.

이후 5개의 동아리 활동 데이터를 POST로 등록하고 다음 흐름을 테스트하였다.

```text
등록 → 전체 조회 → 단건 조회 → 수정
→ 수정 결과 조회 → 삭제 → 삭제한 ID의 404 확인
```

또한 잘못된 입력 요청에서 `400 Bad Request`를 확인하고, `category=친목` 검색으로 확장 기능도 배포 서버에서 확인하였다.

본 프로젝트는 Memory Repository를 사용하므로 Render 서버가 재시작되거나 재배포되면 저장된 데이터가 사라질 수 있다.

---

# ⑦ Weekly Report

## Key Learning

1. Controller, Service, Repository로 역할을 분리하는 Layered Architecture의 흐름을 직접 구현하며 이해하였다.
2. Request DTO와 Response DTO를 Domain 객체와 분리하여 API의 입력과 출력을 관리하는 방법을 학습하였다.
3. REST API의 CRUD뿐 아니라 `400 Bad Request`, `404 Not Found`와 같은 HTTP 상태 코드를 상황에 맞게 반환하는 방법을 이해하였다.

## Problem & Solution

Render 배포 과정에서 Dockerfile을 찾지 못해 빌드가 실패하였다. 오류를 확인한 결과 Dockerfile이 프로젝트 루트에 존재하지 않는 것이 원인이었다. Dockerfile을 올바른 위치에 생성하고 GitHub에 Push한 후 재배포하여 해결하였다.

또한 배포 후 테스트 요청이 `localhost:8080`을 사용하고 있어 연결 거부 오류가 발생하였다. 요청 주소를 실제 Render 배포 URL로 변경하여 해결하였다.

## Code Review

중요하게 작성한 메서드 중 하나는 `ClubActivityService.findByCategory()`이다.

```java
public List<ClubActivityResponse> findByCategory(String category) {
    return repository.findByCategory(category).stream()
            .map(this::toResponse)
            .toList();
}
```

Repository에서 특정 category에 해당하는 Domain 객체들을 조회한 뒤 `toResponse()`를 이용하여 각각 Response DTO로 변환하고 리스트로 반환한다. 이를 통해 Controller가 직접 데이터 검색이나 변환을 수행하지 않고 Service와 Repository가 역할을 분담한다.

## AI Usage

프로젝트 구현 과정에서 AI를 개념 설명, 오류 분석, 코드 검토에 활용하였다.

주요 질문 내용:
- Controller, Service, Repository의 역할
- Request DTO와 Response DTO의 차이
- Memory Repository 구현
- 400 및 404 처리
- JDK와 Gradle 설정
- Dockerfile 작성과 Render 배포 오류
- IntelliJ HTTP Client를 이용한 API 테스트

AI가 제안한 내용은 프로젝트의 패키지명과 클래스 구조에 맞게 직접 확인하고 수정하였다. 이후 IntelliJ에서 직접 실행하여 CRUD, 400, 404, 카테고리 검색과 배포 서버 동작을 확인하였다.

## Reflection

이번 과제를 통해 HTTP 요청이 Controller → Service → Repository → Memory Repository 순서로 처리되고 다시 Response로 반환되는 전체 흐름을 이해할 수 있었다.

또한 로컬에서 프로그램을 실행하는 것과 실제 서버에 배포하는 과정이 다르다는 것을 배웠다. Docker와 Render를 이용하여 실제 배포 URL에서 API를 테스트하면서 배포 과정에 대한 이해를 높일 수 있었다.

앞으로는 Memory Repository 대신 실제 Database를 연결하여 서버가 재시작되어도 데이터가 유지되는 REST API를 구현해 보고 싶다.

## 건의사항

아직은 없습니다.

---

## 제출 전 확인

- [x] 새 Spring Boot 프로젝트
- [x] JDK 23
- [x] Gradle 9.7.1
- [x] 자체 주제 및 id 제외 5개 이상의 필드
- [x] Domain / Request DTO / Response DTO
- [x] Controller → Service → Repository Interface → Memory Repository
- [x] Java Collection 사용
- [x] CRUD 구현 및 ID 자동 생성
- [x] 존재하지 않는 ID → 404
- [x] 잘못된 입력 → 400
- [x] 카테고리별 조회
- [x] 로컬 테스트
- [x] Dockerfile
- [x] Render 배포
- [x] 배포 URL에서 GET / POST / 확장 기능 테스트
- [x] README 작성
- [ ] 자신의 작업 Commit 10회 이상 최종 확인
