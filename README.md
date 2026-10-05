# 22200543 이어진

## 웹 서비스 개발 01분반 - 5주차 과제

### 프로젝트 주제: 동아리 활동 관리 REST API

---

# ① 프로젝트 소개

## 1. 주제와 관리하는 데이터

이번 프로젝트에서는 **동아리 활동 관리 REST API**를 구현하였다.

동아리 활동 하나에 대해 다음 데이터를 관리한다.

| 필드 | 타입 | 설명 |
|---|---|---|
| `id` | `Long` | 활동을 구분하는 고유 ID |
| `title` | `String` | 활동 제목 |
| `clubName` | `String` | 동아리명 |
| `category` | `String` | 활동 분류 |
| `activityDate` | `String` | 활동 날짜 |
| `location` | `String` | 활동 장소 |
| `participants` | `Integer` | 참여 인원 |
| `description` | `String` | 활동 설명 |

`id`는 사용자가 입력하지 않고 등록할 때 서버에서 자동으로 생성한다.

데이터베이스는 사용하지 않고 `ConcurrentHashMap<Long, ClubActivity>`를 이용하여 메모리에 저장하였다.

기본 CRUD 외에 다음 두 가지 기능을 추가하였다.

- **A. 잘못된 입력 처리:** 필수값이 비어 있거나 참여 인원이 1명 미만이면 `400 Bad Request` 반환
- **B. 카테고리 검색:** `category`를 조건으로 활동을 필터링

## 2. 프로젝트 구조

Controller → Service → Repository Interface → Memory Repository → Java Collection 순서로 역할을 분리하였다.

```text
spring_crud_activity
└── src
    └── main
        └── java
            └── org.example.spring_crud_activity
                ├── SpringCrudActivityApplication.java
                ├── controller
                │   └── ClubActivityController.java
                ├── service
                │   └── ClubActivityService.java
                ├── repository
                │   ├── ClubActivityRepository.java
                │   └── ClubActivityMemoryRepository.java
                ├── domain
                │   └── ClubActivity.java
                ├── dto
                │   ├── ClubActivityRequest.java
                │   └── ClubActivityResponse.java
                └── exception
                    ├── ActivityNotFoundException.java
                    └── GlobalExceptionHandler.java
```

| 클래스 | 역할 |
|---|---|
| `ClubActivityController` | HTTP 요청을 받고 Service를 호출하여 HTTP 응답을 반환 |
| `ClubActivityService` | CRUD 비즈니스 로직과 입력값 검증 처리 |
| `ClubActivityRepository` | 저장·조회·수정·삭제 기능을 정의하는 Repository Interface |
| `ClubActivityMemoryRepository` | `ConcurrentHashMap`을 이용하여 실제 데이터 저장 |
| `ClubActivity` | 동아리 활동 데이터를 표현하는 Domain 객체 |
| `ClubActivityRequest` | 등록·수정 요청 데이터를 전달하는 Request DTO |
| `ClubActivityResponse` | API 응답 데이터를 전달하는 Response DTO |
| `ActivityNotFoundException` | 존재하지 않는 ID를 처리하는 예외 |
| `GlobalExceptionHandler` | 예외를 HTTP 상태 코드로 변환 |

## 3. 로컬 실행 방법

1. IntelliJ IDEA에서 `spring_crud_activity` 프로젝트를 연다.
2. 프로젝트의 JDK와 Gradle JVM을 Java 23으로 설정한다.
3. `SpringCrudActivityApplication.java`를 실행한다.
4. 서버가 실행되면 `http://localhost:8080`에서 API를 사용할 수 있다.
5. 프로젝트 루트의 `test.http`를 IntelliJ HTTP Client로 실행하여 API를 테스트한다.

## 4. API Endpoint 표

| Method | URL | 기능 |
|---|---|---|
| `POST` | `/api/activities` | 동아리 활동 등록 |
| `GET` | `/api/activities` | 전체 동아리 활동 조회 |
| `GET` | `/api/activities/{id}` | 특정 동아리 활동 조회 |
| `PUT` | `/api/activities/{id}` | 동아리 활동 수정 |
| `DELETE` | `/api/activities/{id}` | 동아리 활동 삭제 |
| `GET` | `/api/activities?category={category}` | 활동 분류별 검색 |

## 5. 요청·응답 JSON 예시

### POST 요청

```http
POST http://localhost:8080/api/activities
Content-Type: application/json
```

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

### POST 응답

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

## 6. GitHub Repository URL과 배포 URL

- GitHub Repository: **[제출 전 실제 Repository URL 입력]**
- 배포 URL: **[배포 후 실제 URL 입력]**

---

# ② 개발환경 및 Dependency

## 1. 개발환경

| 항목 | 작성 내용 |
|---|---|
| IDE | IntelliJ IDEA |
| JDK | Java 23 |
| Spring Boot | 4.1.1 |
| Build Tool | Gradle |
| 데이터 저장 | `ConcurrentHashMap<Long, ClubActivity>` |
| 배포 환경 | 수업에서 사용한 배포 방식 및 실제 배포 URL 작성 |

## 2. Dependency

### `spring-boot-starter-webmvc`

REST API를 구현하기 위해 사용하였다.

`@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` 등을 사용하여 HTTP 요청을 처리할 수 있다.

### `spring-boot-starter-webmvc-test`

Spring MVC 기반의 테스트 기능을 사용하기 위해 포함하였다.

### `junit-platform-launcher`

Gradle에서 JUnit 기반 테스트를 실행할 때 필요한 테스트 실행 환경이다.

### Java Collection

별도의 Database를 사용하지 않는 과제 조건에 따라 `ConcurrentHashMap<Long, ClubActivity>`를 데이터 저장소로 사용하였다.

---

# ③ Solution 분석

## Q1. Controller에서 들어온 요청은 어떤 순서로 처리되는가?

**답변**

요청은 `ClubActivityController`에서 받아 `ClubActivityService`를 호출하고, Service는 `ClubActivityRepository`를 통해 `ClubActivityMemoryRepository`의 저장 기능을 사용한다.

최종적으로 `ConcurrentHashMap<Long, ClubActivity>`에 데이터가 저장된다.

즉 다음과 같은 흐름이다.

```text
Controller
    ↓
Service
    ↓
Repository Interface
    ↓
Memory Repository
    ↓
Java Collection
```

관련 클래스와 메서드는 `ClubActivityController.create()`, `ClubActivityService.create()`, `ClubActivityMemoryRepository.save()`이다.

## Q2. Request DTO와 Domain 객체와 Response DTO의 역할은 무엇인가?

**답변**

`ClubActivityRequest`는 클라이언트가 등록하거나 수정할 때 보내는 요청 데이터를 전달한다.

`ClubActivity`는 실제 서비스 내부에서 동아리 활동 데이터를 표현하는 Domain 객체이다.

`ClubActivityResponse`는 서버가 클라이언트에게 응답할 데이터를 표현한다.

관련 클래스는 `ClubActivityRequest`, `ClubActivity`, `ClubActivityResponse`이다.

등록 과정에서는 `ClubActivityService.create()`에서 Request DTO의 값을 이용하여 `ClubActivity`를 생성하고, `toResponse()`를 통해 Response DTO로 변환한다.

## Q3. 새 데이터의 ID는 어디에서 생성되는가?

**답변**

새 데이터의 ID는 `ClubActivityMemoryRepository.save()`에서 생성된다.

다음과 같이 `AtomicLong sequence`의 값을 1 증가시켜 새로운 ID를 만들고 Domain 객체에 설정한다.

```java
Long id = sequence.incrementAndGet();
activity.setId(id);
activities.put(id, activity);
```

따라서 클라이언트가 ID를 직접 입력하지 않아도 등록할 때 자동으로 ID가 생성된다.

관련 클래스와 메서드는 `ClubActivityMemoryRepository.save()`이다.

## Q4. 존재하지 않는 ID를 조회하면 어떻게 404가 반환되는가?

**답변**

`ClubActivityService.findById()`에서 Repository의 `findById()` 결과를 확인한다.

해당 ID가 없으면 `ActivityNotFoundException`을 발생시킨다.

그 후 `GlobalExceptionHandler`의 `handleActivityNotFoundException()`이 이 예외를 받아 `HttpStatus.NOT_FOUND`, 즉 `404 Not Found`로 변환한다.

관련 클래스와 메서드는 다음과 같다.

- `ClubActivityService.findById()`
- `ActivityNotFoundException`
- `GlobalExceptionHandler.handleActivityNotFoundException()`

수정과 삭제에서도 존재하지 않는 ID를 확인하여 같은 예외 처리를 사용한다.

## Q5. Domain 객체를 Response DTO로 변환하는 과정은 어떻게 이루어지는가?

**답변**

`ClubActivityService`의 `toResponse()` 메서드에서 `ClubActivity`의 각 필드를 읽어 `ClubActivityResponse`를 생성한다.

```java
private ClubActivityResponse toResponse(ClubActivity activity) {
    return new ClubActivityResponse(
            activity.getId(),
            activity.getTitle(),
            activity.getClubName(),
            activity.getCategory(),
            activity.getActivityDate(),
            activity.getLocation(),
            activity.getParticipants(),
            activity.getDescription()
    );
}
```

따라서 Domain 객체를 그대로 Controller의 응답으로 사용하지 않고 Response DTO로 변환하여 반환한다.

관련 클래스와 메서드는 `ClubActivityService.toResponse()`이다.

## Q6. Repository Interface와 Memory Repository를 분리한 이유는 무엇인가?

**답변**

`ClubActivityRepository`에서는 `save()`, `findAll()`, `findById()`, `update()`, `delete()` 등의 저장소 기능을 정의한다.

실제 구현은 `ClubActivityMemoryRepository`가 담당한다.

이렇게 Interface와 구현을 분리하면 Service는 실제 저장 방식이 무엇인지 직접 알 필요가 없고 Repository를 통해 데이터를 사용할 수 있다.

관련 클래스는 `ClubActivityRepository`와 `ClubActivityMemoryRepository`이며, Service의 생성자에서 `ClubActivityRepository`를 주입받는다.

---

# ④ 개발 과정 요약

## 1단계. 프로젝트 생성 및 기본 설정

IntelliJ IDEA에서 Spring Boot 프로젝트를 생성하고 Java 23, Gradle, Spring WebMVC를 사용하도록 설정하였다.

`SpringCrudActivityApplication`을 실행하여 Spring Boot 프로젝트가 정상적으로 실행되는지 확인하였다.

## 2단계. Domain과 DTO 작성

동아리 활동 관리에 필요한 7개의 데이터를 선정하고 `ClubActivity` Domain 객체를 작성하였다.

이후 요청과 응답을 분리하기 위해 `ClubActivityRequest`, `ClubActivityResponse`를 작성하였다.

등록 요청을 통해 Request DTO의 데이터가 정상적으로 전달되는지 확인하였다.

## 3단계. Repository와 Service 구현

`ClubActivityRepository` Interface를 만들고 `ClubActivityMemoryRepository`에서 `ConcurrentHashMap`을 사용하여 데이터를 저장하도록 구현하였다.

`ClubActivityService`에서는 등록, 전체 조회, 단건 조회, 수정, 삭제 로직을 구현하였다.

등록 후 전체 조회와 단건 조회를 실행하여 데이터가 정상적으로 저장되는지 확인하였다.

## 4단계. Controller와 예외 처리 구현

`ClubActivityController`에서 POST, GET, PUT, DELETE API를 구현하였다.

또한 존재하지 않는 ID를 처리하기 위해 `ActivityNotFoundException`과 `GlobalExceptionHandler`를 작성하였다.

삭제한 ID를 다시 조회하여 `404 Not Found`가 반환되는 것을 확인하였다.

## 5단계. 추가 기능과 테스트 구현

`ClubActivityService.validate()`를 이용하여 잘못된 입력을 검사하고, `ClubActivityRepository.findByCategory()`를 추가하여 카테고리별 검색 기능을 구현하였다.

IntelliJ HTTP Client로 정상 입력, 잘못된 입력, CRUD 전체 흐름, 삭제 후 404, 카테고리 검색을 직접 테스트하였다.

---

# ⑤ 기능 수정·확장

## A. 잘못된 입력 처리

### 1. 기능을 추가한 이유

잘못된 데이터가 저장되는 것을 방지하기 위해 입력값 검증 기능을 추가하였다.

다음 조건을 만족하지 않으면 요청을 거부한다.

- 활동 제목이 비어 있으면 안 된다.
- 활동 날짜가 비어 있으면 안 된다.
- 참여 인원은 1명 이상이어야 한다.

### 2. 수정한 클래스와 메서드

- `ClubActivityService.validate()`
- `ClubActivityService.create()`
- `ClubActivityService.update()`
- `GlobalExceptionHandler.handleIllegalArgumentException()`

`create()`와 `update()`에서 `validate()`를 먼저 호출하도록 구현하였다.

### 3. 테스트 요청과 예상 결과

잘못된 입력:

```http
POST http://localhost:8080/api/activities
Content-Type: application/json
```

```json
{
  "title": "",
  "clubName": "한동 러닝 동아리",
  "category": "운동",
  "activityDate": "2026-10-10",
  "location": "운동장",
  "participants": 0,
  "description": "잘못된 입력 테스트"
}
```

예상 결과:

```text
400 Bad Request
```

정상 입력을 보내면 활동이 정상적으로 등록되어야 한다.

### 4. 실제 결과

잘못된 제목과 참여 인원 `0`을 포함한 요청을 실행한 결과 `400 Bad Request`가 반환되는 것을 확인하였다.

---

## B. 카테고리별 조회 기능

### 1. 기능을 추가한 이유

전체 활동을 조회하는 것뿐만 아니라 활동의 분류를 기준으로 원하는 활동만 확인할 수 있도록 하기 위해 추가하였다.

### 2. 수정한 클래스와 메서드

- `ClubActivityController.findAll()`
- `ClubActivityService.findByCategory()`
- `ClubActivityRepository.findByCategory()`
- `ClubActivityMemoryRepository.findByCategory()`

Controller에서 `category` Query Parameter가 있으면 `findByCategory()`를 호출하도록 구현하였다.

Memory Repository에서는 Stream의 `filter()`를 사용하여 category가 일치하는 활동만 반환한다.

### 3. 테스트 요청과 예상 결과

```http
GET http://localhost:8080/api/activities?category=친목
```

예상 결과:

`category`가 `친목`인 활동만 응답에 포함되어야 한다.

### 4. 실제 결과

여러 개의 테스트 데이터를 등록한 후 `category=친목`으로 조회한 결과, `친목`으로 등록된 활동만 반환되는 것을 확인하였다.

---

# ⑥ 배포 과정 요약

> **주의: 아래 항목은 실제 배포를 완료한 후 실제 명령어와 결과로 수정해야 한다.**

## 1. 빌드 및 배포 순서

실제 배포 시 다음 순서로 진행한다.

```text
Gradle Build
    ↓
빌드 결과 확인
    ↓
수업에서 사용한 배포 환경에 업로드
    ↓
서버 실행
    ↓
배포 URL 접속
    ↓
GET /api/activities 테스트
    ↓
POST /api/activities 테스트
    ↓
STEP 5 기능 테스트
```

## 2. 배포를 위해 추가하거나 수정한 파일·설정

- `build.gradle`
- `application.properties`
- 배포 환경에서 필요한 실행 설정

실제 배포 방식에 따라 변경한 파일과 설정을 배포 후 구체적으로 작성한다.

## 3. 배포 중 발생한 문제와 해결 방법

**배포 후 실제 발생한 문제를 작성한다.**

- 발생한 문제:
- 원인:
- 해결 방법:

## 4. 배포 URL로 확인한 요청과 응답

배포 URL:

```text
[실제 배포 URL 입력]
```

GET 요청:

```http
GET [실제 배포 URL]/api/activities
```

예상 결과:

```text
200 OK
```

POST 요청:

```http
POST [실제 배포 URL]/api/activities
Content-Type: application/json
```

```json
{
  "title": "배포 테스트 활동",
  "clubName": "테스트 동아리",
  "category": "친목",
  "activityDate": "2026-10-10",
  "location": "한동대학교",
  "participants": 10,
  "description": "배포 서버 POST 테스트"
}
```

실제 응답:

```text
[배포 후 실제 응답 입력]
```

---

# ⑦ Weekly Report

## Key Learning

### 1. Layered Architecture

Controller, Service, Repository를 각각 분리하여 구현하면서 각 계층이 어떤 역할을 담당하는지 이해하였다.

특히 Controller가 모든 로직을 직접 처리하는 것이 아니라 Service를 호출하고, Service가 Repository를 사용하는 구조를 직접 구현하였다.

### 2. REST API와 HTTP Method

POST, GET, PUT, DELETE를 각각 등록, 조회, 수정, 삭제 기능에 연결하여 REST API가 실제로 동작하는 과정을 이해하였다.

### 3. Memory Repository

Database 없이 `ConcurrentHashMap`을 사용하여 데이터를 저장하면서 Memory Repository의 동작 방식을 이해하였다.

특히 `save()`에서 ID를 생성하고 `findById()`를 통해 데이터를 찾는 과정을 직접 구현하였다.

## Problem & Solution

개발 과정에서 Java 버전과 Gradle 설정을 맞추는 과정에서 문제가 발생하였다.

프로젝트에서 Java 23을 사용하도록 설정하였지만 IntelliJ의 프로젝트 JDK와 Gradle JVM 설정이 일치하지 않으면 Gradle 프로젝트를 정상적으로 불러오지 못할 수 있었다.

IntelliJ의 Project SDK와 Gradle JVM 설정을 확인하고 Java 23으로 맞춘 후 프로젝트를 다시 동기화하여 해결하였다.

또한 REST API 프로젝트에 맞게 WebMVC Dependency를 사용하도록 Gradle 설정을 확인하였다.

## Code Review

이번 프로젝트에서 중요하게 작성한 메서드는 `ClubActivityService.findByCategory()`이다.

```java
public List<ClubActivityResponse> findByCategory(String category) {
    return repository.findByCategory(category).stream()
            .map(this::toResponse)
            .toList();
}
```

이 메서드는 Repository에서 category가 일치하는 `ClubActivity`를 조회한 뒤 Stream을 이용하여 각각의 Domain 객체를 `ClubActivityResponse`로 변환한다.

이를 통해 추가 조회 기능에서도 Service가 Repository와 Controller 사이에서 데이터를 전달하고 Response DTO로 변환하는 역할을 수행하도록 하였다.

## AI Usage

프로젝트 구현 과정에서 AI를 개념 설명, 코드 구조 확인, 오류 분석 및 코드 검토에 활용하였다.

주로 다음과 같은 내용을 질문하였다.

- Controller → Service → Repository 구조의 역할
- Request DTO와 Response DTO의 차이
- Java Collection을 사용하는 Memory Repository 구현 방법
- 존재하지 않는 ID에 대한 404 처리 방법
- REST API 테스트 및 오류 원인 분석
- Gradle 및 Spring Boot 설정 오류 확인

AI가 제안한 코드를 그대로 제출하지 않고 현재 프로젝트의 패키지명과 클래스명에 맞게 수정하였다.

또한 IntelliJ에서 프로젝트를 직접 실행하고 HTTP 요청을 보내 CRUD, 400 Bad Request, 404 Not Found, category 검색이 실제로 동작하는지 확인하였다.

## Reflection

이번 과제를 통해 Spring Boot에서 REST API를 계층별로 나누어 구현하는 기본 구조를 이해할 수 있었다.

앞으로는 현재 Java Collection을 사용하는 방식에서 Database를 사용하는 방식으로 변경했을 때 Repository 구조가 어떻게 달라지는지 공부해 보고 싶다.

또한 현재 직접 구현한 입력값 검증을 Spring Boot의 Validation 기능을 사용하여 처리하는 방법과 API 테스트를 자동화하는 방법도 추가로 학습하고 싶다.

## 건의사항

실습 과정에서 배포 단계에서 발생할 수 있는 오류와 해결 방법을 조금 더 구체적인 예시와 함께 제공해 주면 좋을 것 같다.

---

## 제출 전 최종 확인

- [x] ① 프로젝트 소개
- [x] ② 개발환경 및 Dependency
- [x] ③ Solution 분석 Q&A 5개 이상
- [x] ④ 개발 과정 요약
- [x] ⑤ 기능 수정·확장 A
- [x] ⑤ 기능 수정·확장 B
- [x] ⑥ 배포 과정 요약 틀
- [x] ⑦ Weekly Report
- [ ] 실제 GitHub Repository URL 입력
- [ ] 실제 배포 URL 입력
- [ ] 실제 Gradle 버전 확인 및 입력
- [ ] 실제 배포 과정 및 결과 입력
- [ ] Git 사용자 작성 Commit 10회 이상 확인
