# N-BUY
![test coverage](.github/badges/jacoco.svg)
![logo](/src/main/resources/static/img/logo.jpg)
<br/>

<!-- ABOUT THE PROJECT -->
## 👩‍💻 프로젝트 주제
### 지역 주민을 위한 '같이 구매' 서비스
<br/>

## 📩  프로젝트 기획 배경
- 혼자 지내면서 마트에서 물건을 살 때 용량이 너무 많아 부담이 되었던 적은 없으신가요?
- 요리를 위해 특정 재료가 필요한데, 필요 이상의 양을 사야할 때 불편했던 적은 없으신가요?
- 과일을 먹고 싶은 날, 박스 단위로만 팔아 구매에 고민이 되셨던 적은 없으신가요?
> 지역 주민들이 대용량 상품에 대한 부담과 배달비를 절감할 수 있도록 ‘같이구매’ 서비스를 개발하게 되었습니다.
<br/>

## 📁 기술 스택

<div align=center> 
<br>
    <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"> 
    <img src="https://img.shields.io/badge/Mysql-4479A1?style=for-the-badge&logo=Mysql&logoColor=white">
    <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
    <img src="https://img.shields.io/badge/Elasticsearch-005571?style=for-the-badge&logo=Elasticsearch&logoColor=white">
<br>
    <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=Thymeleaf&logoColor=white">
    <img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> 
    <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white">
    <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
</div>
<br>

## 📋 상세 기능
> 공통 (Common)
- [X] 회원 가입 및 가입인증 이메일
  - [X] JavaMailSender & Google SMTP 사용
  - [X] 우편번호 API 적용 [(📩 우편번호 서비스)](https://postcode.map.daum.net/guide)
- [X] 로그인 및 로그아웃
  - [X] Spring Security 설정
- [X] 탈퇴

> 일반 회원 (User)
- [X] 상품 등록 & 수정 & 취소
- [X] 공동구매 신청 & 취소
- [X] 상품 검색
- [X] 내 정보 확인
  - [X] 등록 상품 확인 (진행 상태 확인)
  - [X] 구매 이력 확인 (진행 상태 확인)

> 관리자 (Back Office)
- [X] 회원 관리
- [X] 카테고리 관리
- [X] 상품 관리
<br/>

------------

## 🪟 Project Structure
### | 구현 및 배포 작업 처리 과정
![STRUCTURE](/src/main/resources/static/img/structure.jpg)
<br/>


## 📚 ERD
![ERD](/src/main/resources/static/img/ERD.jpg)
> 추가 및 변경 되는 서비스에 따라 ERD 변경 될 수 있습니다.
<br/>


## 🎯 Trouble Shooting
> [N-BUY : 트러블 슈팅](https://agate-lady-913.notion.site/Trouble-Shooting-015fd9304ffd476d98b47c64b1dfe27e)
<br/>