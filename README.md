# 22200543 이어진

## 웹 서비스 개발 01분반 - 5주차 과제

### 프로젝트 주제: 동아리 활동 관리 프로그램

## 1. 프로젝트 소개

이번 과제에서는 Spring Boot를 이용하여 동아리 활동 정보를 관리하는 REST API 기반의 CRUD 프로그램을 구현하였다.

동아리 활동의 제목, 동아리명, 활동 분류, 활동 날짜, 장소, 참여 인원, 설명 등의 정보를 관리하며, REST API를 통해 동아리 활동의 등록, 전체 조회, 개별 조회, 수정, 삭제 기능을 수행할 수 있도록 구현하였다.

또한 프로그램의 각 기능을 Controller, Service, Repository로 나누어 Layered Architecture 구조를 적용하였다.

추가적으로 활동 분류(category)를 기준으로 동아리 활동을 검색할 수 있는 기능을 구현하였다.

---

## 2. 프로젝트 구조

프로그램의 기능을 하나의 클래스에 모두 작성하지 않고 각 역할을 분리하여 Layered Architecture를 적용하였다.

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

- `ClubActivityController` : HTTP 요청을 받고 응답을 반환
- `ClubActivityService` : 동아리 활동 CRUD 및 비즈니스 로직 처리
- `ClubActivityRepository` : 데이터 처리 기능을 정의한 인터페이스
- `ClubActivityMemoryRepository` : Java Collection을 이용하여 실제 데이터 저장
- `ClubActivity` : 동아리 활동 데이터를 저장하는 도메인 클래스
- `ClubActivityRequest` : API 요청 데이터를 전달하는 DTO
- `ClubActivityResponse` : API 응답 데이터를 전달하는 DTO
- `ActivityNotFoundException` : 존재하지 않는 활동을 조회할 때 발생하는 예외
- `GlobalExceptionHandler` : 잘못된 입력과 존재하지 않는 데이터에 대한 HTTP 상태 코드 처리

## 3. Request / Response JSON 예시

### Request

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

### Response

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

## 4. API Endpoint

| Method | URL | 기능 |
|--------|-----|------|
| `POST` | `/api/activities` | 동아리 활동 등록 |
| `GET` | `/api/activities` | 전체 동아리 활동 조회 |
| `GET` | `/api/activities/{id}` | 특정 동아리 활동 조회 |
| `PUT` | `/api/activities/{id}` | 동아리 활동 정보 수정 |
| `DELETE` | `/api/activities/{id}` | 동아리 활동 삭제 |
| `GET` | `/api/activities?category={category}` | 활동 분류별 검색 |

## 5. 실행방법

### IntelliJ IDEA에서 실행

1. 프로젝트를 IntelliJ IDEA에서 연다.
2. `SpringCrudActivityApplication.java`를 실행한다.
3. Spring Boot 서버가 `localhost:8080`에서 실행되는지 확인한다.
4. 프로젝트 루트의 `test.http` 파일을 실행하여 API를 테스트한다.

### test.http

IntelliJ HTTP Client를 이용하여 POST, GET, PUT, DELETE 등의 API 요청을 테스트하였다.

## 6. Postman 또는 HTTP Client 테스트 결과

| 순서 | Method | URL | 테스트 결과 |
|--|--------|-----|-------------|
| 1 | `POST` | `/api/activities` | `201 Created` |
| 2 | `GET` | `/api/activities` | `200 OK` |
| 3 | `GET` | `/api/activities/1` | `200 OK` |
| 4 | `PUT` | `/api/activities/1` | `200 OK` |
| 5 | `GET` | `/api/activities/1` | `200 OK` |
| 6 | `DELETE` | `/api/activities/1` | `204 No Content` |
| 7 | `GET` | `/api/activities/1` | `404 Not Found` |
| 8 | `GET` | `/api/activities?category=친목` | `200 OK` |
| 9 | `POST` | `/api/activities` (잘못된 입력) | `400 Bad Request` |

### 추가 조회 기능 테스트

`category`를 이용하여 특정 분류의 동아리 활동만 조회할 수 있도록 구현하였다.

```http
GET http://localhost:8080/api/activities?category=친목
```

여러 활동 중 `category`가 `친목`인 활동만 조회되는 것을 확인하였다.

### 잘못된 입력 테스트

활동 제목이 비어 있거나 참여 인원이 1명 미만인 경우 잘못된 입력으로 처리하도록 구현하였다.

잘못된 요청이 들어오면 `400 Bad Request`가 반환되는 것을 확인하였다.

---

## 7. 이번 과제 키워드

REST API, CRUD, `@RequestBody`, `@PathVariable`, `@RequestParam`, Layered Architecture, Repository Interface, DTO, Java Collection, 예외 처리, HTTP Status

---

## 8. 핵심 내용 정리

### REST API와 CRUD

REST API와 HTTP Method `POST`, `GET`, `PUT`, `DELETE`를 이용하여 동아리 활동의 등록, 조회, 수정, 삭제 기능을 구현하였다.

### Layered Architecture

`Controller`, `Service`, `Repository`로 역할을 나누어 각 계층의 책임을 분리하였다.

### Repository Interface

`ClubActivityRepository` 인터페이스를 통해 저장, 조회, 수정, 삭제 및 분류 검색에 필요한 기능을 정의하고 `ClubActivityMemoryRepository`에서 실제로 구현하였다.

### Memory Repository

`ConcurrentHashMap<Long, ClubActivity>`를 사용하여 동아리 활동 데이터를 메모리에 저장하고 ID를 기준으로 데이터를 관리하였다.

### DTO를 이용한 데이터 전달

`ClubActivityRequest`와 `ClubActivityResponse`를 사용하여 API 요청과 응답 데이터를 구분하였다.

### 예외 처리

존재하지 않는 ID를 조회하거나 수정 및 삭제하려는 경우 `ActivityNotFoundException`을 발생시키고 `404 Not Found`를 반환하도록 구현하였다.

또한 잘못된 입력이 들어오는 경우 `400 Bad Request`를 반환하도록 구현하였다.

### 추가 조회 기능

`GET /api/activities?category={category}`를 통해 활동의 `category`를 기준으로 원하는 활동만 조회할 수 있도록 구현하였다.

---

# 9. Weekly Report

## Key Learning

이번 과제를 통해 다음 내용을 학습하였다.

REST API에서 HTTP Method를 이용하여 CRUD 기능을 구현하는 방법을 학습하였다.

Controller, Service, Repository로 역할을 나누는 Layered Architecture를 직접 적용하면서 각 계층의 역할을 이해하였다.

또한 `@RequestBody`, `@PathVariable`, `@RequestParam`을 사용하여 HTTP 요청 데이터를 처리하는 방법을 학습하였다.

추가적으로 Java Collection을 이용하여 별도의 데이터베이스 없이 데이터를 저장하고 조회하는 방법을 구현하였다.

## CRUD Flow

클라이언트가 HTTP Request를 보내면 `ClubActivityController`가 요청을 받는다.

Controller는 요청 데이터를 `ClubActivityService`에 전달하고, Service는 필요한 비즈니스 로직을 처리한 후 `ClubActivityRepository`에 데이터 처리를 요청한다.

`ClubActivityRepository`를 구현한 `ClubActivityMemoryRepository`에서는 `ConcurrentHashMap`을 이용하여 실제 데이터를 저장하고 조회한다.

처리가 완료되면 결과가 다시 Controller를 통해 HTTP Response로 반환된다.

## Problem & Solution

구현 과정에서 Controller, Service, Repository의 역할을 구분하는 부분이 어려웠다.

각 계층에서 어떤 기능을 담당해야 하는지 확인하면서 기능을 분리하여 구현하였다.

또한 잘못된 입력이나 존재하지 않는 ID에 대한 HTTP 상태 코드를 처리하기 위해 `GlobalExceptionHandler`를 사용하였다.

프로젝트를 실행하는 과정에서는 Java JDK 버전과 Gradle 설정을 확인하면서 빌드 환경을 맞추었다.

## Code Review

이번 프로젝트에서 중요한 부분은 Layered Architecture에 따라 각 클래스의 역할을 분리한 것이다.

특히 `ClubActivityRepository`를 인터페이스로 만들고 `ClubActivityMemoryRepository`에서 구현함으로써 데이터 저장 방식을 Repository 계층에서 관리할 수 있도록 하였다.

또한 `ClubActivityRequest`와 `ClubActivityResponse`를 사용하여 API 요청과 응답 데이터를 분리하였다.

## Reflection

이번 과제를 통해 REST API의 HTTP Method가 CRUD 기능과 연결되어 동작하는 과정을 직접 구현해 볼 수 있었다.

또한 단순히 CRUD 기능을 만드는 것뿐만 아니라 Controller, Service, Repository의 역할을 분리하면 프로그램의 구조를 더 명확하게 만들 수 있다는 것을 이해하게 되었다.

특히 이번에는 `category`를 이용한 추가 조회 기능과 잘못된 입력에 대한 예외 처리까지 구현하면서 REST API의 기본적인 기능을 조금 더 다양하게 경험할 수 있었다.

## 건의 사항

아직은 없습니다.

---

# 10. Solution 분석 Q&A

> 아래 내용은 제출 전 제공된 Book CRUD Solution을 실제로 분석한 내용에 맞게 보완한다.

### Q1. Controller의 역할은 무엇인가?

`ClubActivityController`는 클라이언트의 HTTP 요청을 받아 Service 계층으로 전달하고, 처리 결과를 HTTP Response로 반환하는 역할을 한다.

주요 메서드:
- `create()`
- `findAll()`
- `findById()`
- `update()`
- `delete()`

### Q2. Service 계층을 별도로 사용하는 이유는 무엇인가?

`ClubActivityService`에서 CRUD의 비즈니스 로직과 입력값 검증을 처리하도록 하여 Controller가 너무 많은 역할을 담당하지 않도록 하였다.

주요 메서드:
- `create()`
- `findAll()`
- `findById()`
- `update()`
- `delete()`

### Q3. Repository Interface와 Memory Repository를 분리한 이유는 무엇인가?

`ClubActivityRepository`에서는 필요한 데이터 처리 기능을 인터페이스로 정의하고, `ClubActivityMemoryRepository`에서 실제 저장 방법을 구현하였다.

이렇게 분리하면 Service가 구체적인 저장 방식에 직접 의존하지 않고 Repository를 통해 데이터를 처리할 수 있다.

### Q4. DTO를 사용하는 이유는 무엇인가?

`ClubActivityRequest`와 `ClubActivityResponse`를 사용하여 클라이언트의 요청 데이터와 서버의 응답 데이터를 구분하였다.

이를 통해 도메인 객체와 API 데이터 전달을 분리할 수 있다.

### Q5. 존재하지 않는 ID에 대해 어떻게 404를 반환하는가?

`ClubActivityService`에서 `findById()`를 호출했을 때 데이터가 존재하지 않으면 `ActivityNotFoundException`을 발생시킨다.

`GlobalExceptionHandler`에서 해당 예외를 처리하여 `404 Not Found`를 반환한다.

---

# 11. 개발 과정

### 1단계. 프로젝트 생성

Spring Boot 프로젝트를 생성하고 Java 23 및 Gradle 환경을 설정하였다.

### 2단계. 도메인 및 DTO 구현

동아리 활동 데이터를 관리하기 위한 `ClubActivity`를 만들고 `ClubActivityRequest`, `ClubActivityResponse` DTO를 작성하였다.

### 3단계. Repository와 Service 구현

`ClubActivityRepository` 인터페이스와 `ClubActivityMemoryRepository`를 구현하고, Service에서 CRUD 비즈니스 로직을 작성하였다.

### 4단계. Controller 및 예외 처리 구현

REST API 요청을 처리하는 `ClubActivityController`를 구현하고, `ActivityNotFoundException`과 `GlobalExceptionHandler`를 추가하였다.

### 5단계. 추가 기능 및 테스트

`category`를 이용한 검색 기능을 추가하고 IntelliJ HTTP Client의 `test.http`를 이용하여 CRUD, 400, 404 및 추가 조회 기능을 테스트하였다.

---

# 12. GitHub

- GitHub Repository: [제출 전 GitHub Repository 주소 입력]

---

# 13. 배포

- 배포 플랫폼: [제출 전 입력]
- 배포 URL: [제출 전 입력]

### 배포 결과

배포 후 실제 서버 URL을 이용하여 주요 REST API가 정상적으로 동작하는지 확인한다.

---

# 14. AI 활용

프로젝트 구현 과정에서 AI를 활용하여 Spring Boot 프로젝트의 구조와 코드 작성 과정에서 발생한 문제를 확인하고 해결 방법을 참고하였다.

AI의 답변을 그대로 사용하는 것이 아니라 프로젝트의 요구사항에 맞게 코드를 수정하고 IntelliJ에서 직접 실행 및 테스트하였다.

---

# 15. 최종 체크

- [x] Spring Boot 프로젝트 구현
- [x] 동아리 활동 CRUD 구현
- [x] Layered Architecture 적용
- [x] Repository Interface 구현
- [x] Memory Repository 구현
- [x] Java Collection 사용
- [x] Request / Response DTO 사용
- [x] 자동 ID 생성
- [x] 존재하지 않는 ID → 404
- [x] 잘못된 입력 → 400
- [x] category 검색 기능 구현
- [x] IntelliJ HTTP Client를 이용한 로컬 테스트
- [ ] Git 10회 이상 커밋
- [ ] GitHub push
- [ ] 배포
- [ ] 배포 테스트
- [ ] GitHub URL 입력
- [ ] 배포 URL 입력
- [ ] 실제 테스트 결과 캡처 추가
