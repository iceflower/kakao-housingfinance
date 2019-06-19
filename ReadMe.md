> 2019 카카오페이 서버개발자 경력 채용
## 주택 금융 서비스 API 개발 (주택금융 공급현황 분석 서비스)
---
국내 주택금융 신용보증 기관으로부터 년도별 각 금융기관(은행)에서 신용보증한 금액에 대한 데이터가 주어집니다. 이를 기반으로 아래 기능명세에 대한 API 를 개발하고 각 기능별 Unit Test 코드를 개발하세요.


1. 사용 언어 및 프레임워크
    - java 8
    - Spring boot

2. Dependencies
    - spring-boot 2.1.5.RELEASE
    - spring-boot-starter-data-jpa
    - spring-boot-starter-web
    - spring-boot-starter-security
    - spring-boot-starter-mail
    - spring-boot-starter-test
    - spring-security-test
    - querydsl-core 4.2.1
    - querydsl-jpa 4.2.1
    - querydsl-apt 4.2.1
    - h2 database
    - springfox-swagger-ui
    - springfox-swagger2
    - jjwt
    - jackson-datatype-jsr310
    - jackson-mapper-asl

3. 실행법
    - in windows terminal
        <pre>
            <code>
            .\mvnw clean install
            .\mvnw spring-boot:run
            </code>
        </pre>
    - in linux terminal
        <pre>
            <code>
            mvn clean install
            mvn spring-boot:run
            </code>
        </pre>
    - 인텔리제이/이클립스에서 import maven project를 선택 후 진행.

4.  API 명세
    - sawgger 문서 (프로젝트 실행 후 아래 주소로 접근)
        - http://localhost:9004/swagger-ui.html#
        - http://localhost:9004/v2/api-docs
