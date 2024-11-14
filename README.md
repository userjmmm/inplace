# Inplace ( Team7_BE )

<p align="center">
  <img src="https://i.ibb.co/CVBQHPg/image-2.png" alt="메인 이미지">
</p>

## 목차

> 1. [**_프로젝트 목적_**](#프로젝트-목적)  
> 2. [**_서비스 정보_**](#서비스-정보)  
> 3. [**_주요 기능_**](#주요-기능)
> 4. [**_개발 기간_**](#개발-기간)
> 5. [**_배포 주소_**](#배포-주소)
> 6. [**_팀원_**](#팀원)
> 7. [**_프로그램 구조_**](#프로그램-구조)  
> 8. [**_API & ERD_**](#api--erd)  
> 9. [**_저장소 정보_**](#저장소-정보)  
> 5. [**_프로그램 구조_**](#프로그램-구조)  
> 10. [**_주요 종속성 버전_**](#주요-종속성-버전)  
> 11. [**_기술 스택_**](#기술-스택)
> 12. [**_기능 흐름_**](#기능-흐름) 
> 13. [**_테스트_**](#테스트)   
> 14. [**_사용 예시_**](#사용-예시)  


## 프로젝트 목적

> 저희의 아이디어는 **데이트 코스의 단조로움**을 어떻게 하면 해소할 수 있을까? 라는 생각에서 시작했습니다.
>
> 아이디어를 구체화하는 과정에서 저희는 **장소**에 관련된 소재로 인스타, 유튜브 등의 SNS 및 동영상 플랫폼의 성장과 함께 등장한 **인플루언서** 라는 개념에 집중하게
> 되었습니다.
>
> 이는 **인플루언서가 방문한 장소에 대한 정보를 서비스 해보자!** 라는 생각으로 이어졌습니다.
>
> 이를 관심있는 **인플루언서를 등록하고, 이에 따른 장소 추천 및, 장소에 대한 리뷰 기능**을 통해 풀어내어 **Inplace** 라는 저희만의 웹 애플리케이션으로
> 구현해보았습니다.

## 서비스 정보

> 긴 영상은 필요 없어요 인플루언서가 다녀간 쿨플, 한눈에 쏙!

1. **회원 가입 및 로그인** 기능
    - 카카오를 이용한 회원 가입 및 로그인
    - 회원 닉네임 변경 가능
2. **관심있는 인플루언서 설정** 기능
    - 최초 로그인 시, 관심 인플루언서를 설정 가능
    - 이후, 인플루언서 페이지에서 좋아요 버튼을 통해 설정 가능
    - 마이 페이지에서 관심있는 인플루언서 삭제 가능
3. **장소 검색 및 좋아요** 기능
    - 지도를 통한 장소 검색 가능
        - 지역, 인플루언서, 장소 태그를 통한 검색 가능
    - 장소 세부 정보 열람 및 장소 좋아요 가능
    - 장소에 대한 정보를 모바일로 받기 가능 ( 카카오톡 나에게 메세지 보내기 )
4. **리뷰** 기능
    - 정보 받은 후 3일 뒤 리뷰 페이지 전송 ( 카카오톡 나에게 메세지 보내기 )
    - 리뷰 확인 및 수정 가능

## 주요 기능

> Inplace의 주요 기능

1. **회원가입 및 로그인**
    - Spring Security를 이용한 OAuth 2.0 카카오 로그인 기능을 사용합니다
    - 쿠키에 Access, Refresh Token을 담아 사용하며, Refresh 동작을 수행할 수 있습니다
2. **현재 위치 기반 서비스**
    - 웹 페이지의 위치 정보 사용에 동의시, 카카오 API와 내 위치 정보를 사용하여 주변의 장소 정보와, 자동 지도 위치 설정을 사용할 수 있습니다
3. **통합 검색 서비스**
    - Elastic Search를 사용한 인플루언서, 장소, 비디오 이름에 대한 통합 검색 기능을 사용할 수 있습니다
4. **관심 인플루언서 등록 및 이를 토대로 한 서비스**
    - 최초 로그인 시 & 인플루언서 페이지에서 관심 인플루언서를 등록할 수 있습니다
    - 이를 토대로 메인 페이지에서 관심 인플루언서의 최신 방문 장소를 확인할 수 있습니다
5. **지도 기반 검색 서비스**
    - 지도 API를 이용하여 장소를 검색할 수 있습니다
        - 관심 등록하지 않은 인플루언서 및 주소, 장소 태그를 이용하여 세부 검색이 가능합니다
6. **장소 세부 정보 서비스**
    - 장소의 세부 정보를 열람할 수 있습니다
    - 장소에 달린 다른 유저들의 리뷰를 확인할 수 있습니다
    - 장소에 대한 좋아요 기능을 사용할 수 있습니다
    - 장소 세부 페이지에서 장소에 대한 정보를 카카오톡 메세지로 받아 볼 수 있습니다
7. **리뷰 기능**
    - 장소 정보를 받은 후 3일 뒤, 해당 장소에 대한 리뷰 링크를 받아 리뷰를 작성할 수 있습니다
    - 위 기능들은 카카오톡 메세지 보내기 API를 이용하며, 카카오톡으로 전송되는 링크는 모바일 뷰를 지원합니다
8. **마이 페이지 기능**
    - 좋아요 표시한 장소, 인플루언서를 관리할 수 있습니다
    - 내가 작성한 리뷰를 관리할 수 있습니다
    - 사용자 닉네임을 변경할 수 있습니다

## 개발 기간

> 2024.08.22 ~ 2024.11.15

## 배포 주소

> [**BackEnd**](https://api.inplace.my) : _api.inplace.my_
>
> [**FrontEnd**](https://inplace.my) : _inplace.my_

## 팀원

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<table>
  <tr>
    <td align="center">
       <b>Frontend</b><br />
    </td>
    <td align="center">
       <b>Frontend</b><br />
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/userjmmm"><img src="https://avatars.githubusercontent.com/u/141299582?v=4" width="80px;" alt=""/><br /><sub><b>이정민</b></sub></a>
    </td>
    <td align="center">
      <a href="https://github.com/Hyoeunkh"><img src="https://avatars.githubusercontent.com/u/102338613?v=4" width="80px;" alt=""/><br /><sub><b>이효은</b></sub></a>
    </td>
  </tr>
</table>
<table>
  <tr>
    <td align="center">
      <b>Backend</b><br />
    </td>
    <td align="center">
      <b>Backend</b><br />
    </td>
    <td align="center">
      <b>Backend</b><br />
    </td>
    <td align="center">
      <b>Backend</b><br />
    </td>
    <td align="center">
      <b>Backend</b><br />
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/sanghee0820"><img src="https://avatars.githubusercontent.com/u/102018082?v=4" width="80px;" alt=""/><br /><sub><b>이상희</b></sub></a>
    </td>
    <td align="center">
      <a href="https://github.com/dong-yxxn"><img src="https://avatars.githubusercontent.com/u/129285999?v=4" width="80px;" alt=""/><br /><sub><b>김동윤</b></sub></a>
    </td>
    <td align="center">
      <a href="https://github.com/suhyeon7497"><img src="https://avatars.githubusercontent.com/u/137245467?v=4" width="80px;" alt=""/><br /><sub><b>정수현</b></sub></a>
    </td>
    <td align="center">
      <a href="https://github.com/wndlthsk"><img src="https://avatars.githubusercontent.com/u/80496766?v=4" width="80px;" alt=""/><br /><sub><b>우현서</b></sub></a>
    </td>
    <td align="center">
      <a href="https://github.com/BaeJunH0"><img src="https://avatars.githubusercontent.com/u/114082026?v=4" width="80px;" alt=""/><br /><sub><b>배준호</b></sub></a>
    </td>
  </tr>
</table>
<!-- ALL-CONTRIBUTORS-LIST:END -->

## 프로그램 구조

## API & ERD

![image](https://github.com/user-attachments/assets/ce11ff39-8294-4eda-aa58-7b5bd0ac1620)

> [**API Spec**](https://www.notion.so/API-9e96d1ef1475414b861a50d0e4ca366e) :
_https://www.notion.so/API-9e96d1ef1475414b861a50d0e4ca366e_

![image](https://github.com/user-attachments/assets/17cedf5c-c554-4723-9b08-6635005ea0b2)

> [**ERD**](https://www.notion.so/ERD-36ec8e40cb264abe87588e97ae77ac55) :
> https://www.notion.so/ERD-36ec8e40cb264abe87588e97ae77ac55

## 저장소 정보

> **Using Language**
>
![GitHub language count](https://img.shields.io/github/languages/count/kakao-tech-campus-2nd-step3/Team7_BE)
![GitHub top language](https://img.shields.io/github/languages/top/kakao-tech-campus-2nd-step3/Team7_BE)

> **Repo, Code Volume**
>
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/kakao-tech-campus-2nd-step3/Team7_BE)
![GitHub repo size](https://img.shields.io/github/repo-size/kakao-tech-campus-2nd-step3/Team7_BE)

> **Commit Avg**
>
![GitHub commit activity](https://img.shields.io/github/commit-activity/w/kakao-tech-campus-2nd-step3/Team7_BE)

> **Issues**
>
![GitHub open issues](https://img.shields.io/github/issues/kakao-tech-campus-2nd-step3/Team7_BE)
![GitHub closed issues](https://img.shields.io/github/issues-closed/kakao-tech-campus-2nd-step3/Team7_BE)

> **PRs**
>
![GitHub pull requests](https://img.shields.io/github/issues-pr/kakao-tech-campus-2nd-step3/Team7_BE?label=open%20pull%20requests)
![GitHub closed pull requests](https://img.shields.io/github/issues-pr-closed/kakao-tech-campus-2nd-step3/Team7_BE?label=closed%20pull%20requests)

## 주요 종속성 버전

> **Spring Boots 3.3.3**
>
> **Java 17 LTS**

## 기술 스택

> **Backend**
>
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-DC382D?style=flat-square&logo=lombok&logoColor=white)

> **Security**
>
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=flat-square&logo=jsonwebtokens&logoColor=white)

> **DB**
>
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=flat-square&logo=spring&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-0055a2?style=flat-square&logo=appveyor&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white)

> **Web**
>
![Spring WebFlux](https://img.shields.io/badge/Spring%20WebFlux-6DB33F?style=flat-square&logo=spring&logoColor=white)

> **Deployment**
>
![AWS EC2](https://img.shields.io/badge/AWS%20EC2-FF9900?style=flat-square&logo=amazonaws&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white)

> **Admin Page**
>
![jQuery](https://img.shields.io/badge/jQuery-0769AD?style=flat-square&logo=jquery&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=flat-square&logo=thymeleaf&logoColor=white)
![AJAX](https://img.shields.io/badge/AJAX-005571?style=flat-square&logo=ajax&logoColor=white)

> **Test**
>
![JMeter](https://img.shields.io/badge/JMeter-D22128?style=flat-square&logo=apachejmeter&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-25A162?style=flat-square&logo=junit5&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-FFCA28?style=flat-square&logo=mockito&logoColor=white)

> **Code Maintenance**
>
![Git](https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=github&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=flat-square&logo=githubactions&logoColor=white)

> **Collaboration Tool**
>
![Discord](https://img.shields.io/badge/Discord-5865F2?style=flat-square&logo=discord&logoColor=white)
![Slack](https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=slack&logoColor=white)

## 테스트

> **테스트 시나리오** : https://www.notion.so/9ed68b292c004fc69f7eaad513054d96
>
> **테스트 결과보고서** : https://www.notion.so/08a520d3b8c44154a19425b0bcc16f6f

## 기능 흐름

- ### Spring Security
    - oauth 로그인 시, jwt로 accessToken과 refreshToken을 Cookie에 담아줍니다.
        - oauthToken은 aes알고리즘으로 암호화 되어 db에 저장됩니다.
    - 모든 요청은 AuthorizationFilter에서 Cookie에 있는 토큰이 유효한지 확인하고, 유효하면 Authenticate합니다.
- ### PlaceMessage
    - 요청이 오면 webClient(비동기)로 나에게보내기 kakao api를 통해 장소 정보를 보냅니다.
    - 3일 뒤 나에게 보내기 kakao api를 통해 리뷰 요청 메세지를 보냅니다.
- ### TokenRefresh
    - Redis DB에 username(key)로 refreshToken을 확인하고, RTR (Refresh Token Rotation)을 합니다.

## 사용 예시

> 실제 유저 사용 페이지 흐름 보여주기 ( 영상 x )
