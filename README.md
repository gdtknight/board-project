# 강의 실습 및 개인 학습을 위한 게시판 프로젝트
- 패스트캠퍼스 강의 실습
## 프로젝트 목표
- 기획, 문서 작성, 개발, 형상 관리, 테스트, 배포에 이르는 전반적인 개발 프로세스 실습
- 이해하기 쉬운 소재로 명확한 기능적 요구사항을 작성하는 연습
- 요구사항을 바탕으로 실제 구현에 도움이 되는 각종 문서 작업 경험
- Java, Spring Boot를 기반으로 요구사항을 실제로 구현하는 기술적인 방법 습득
- 최신 버전의 기술 사용을 통해 기술 동향 파악 및 새로운 문제와 해결 방법 확인

# 실습 진행 과정 및 내용 기록
## 개발 환경
- OS : Ubuntu Linux 22.0.4
- TestEditor : Neovim v0.8.0
- Version Control : Git
- Git Hosting : GitHub
## 주요 기술 스택
- Java : Temurin-17.0.5
- Spring Boot : 2.7.5
- Build : Gralde 7.5.1
- Test : JUnit 5
- Deploy : Heroku
## 주요(예상) 기능 및 사용(예상) 기술 (Java + Spring Boot 기반)
- 웹 서비스 제공                          -> Spring Web
- 게시판, 댓글 도메인의 설계              -> Diagrams.net, Google Sheet
- 도메인 설계, 데이터 DB 저장             -> Spring Data JPA, H2 Database, MariaDB
- Json API 로 데이터 제공                 -> Rest Repositories, Rest Repositories HAL Explorer
- 웹 화면으로 서비스 제공 + 디자인 요소   -> Thymeleaf + Bootstrap 5.2
  - 게시판 페이지
  - 게시글 페이지
  - 로그인 페이지
- 입출력 데이터 검증                      -> Validation
- 인증 기능                               -> Spring Security
- 생산성                                  -> Lombok, Spring Boot DevTools, Spring Boot Actuator
