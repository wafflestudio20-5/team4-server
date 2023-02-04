<!-- HEADER -->
  ![rect](https://capsule-render.vercel.app/api?type=rect&color=gradient&text=%20TEAM4%20&fontAlign=27&fontSize=30&textBg=true&desc=Welcome%20to%20%27team4-server%27%20Reopository!&descAlign=62&descAlignY=50)
  
   
<!-- PROJECT LOGO -->

<br />
<div align="center">
  <a href="https://github.com/wafflestudio/snutt-ios"><img src="https://user-images.githubusercontent.com/90292371/216655038-faca7e09-9012-4e56-a0a0-5127655dedc8.png" alt="Logo" width="110" height="110">
  </a>
  <h3 align="center">MUSIN4A</h3>

  <p align="center">
    Wafflestudio Rookies 20.5th, Toy Project: MUSINSA clone-coding (22.12.18.-23.02.04.)
    <div style=" padding-bottom: 1rem;">
    <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"/>
    <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/>
    </div>
  </p>
  <a href="https://bold-tin-2e7.notion.site/MUSIN4-73b8495ff4eb487fa03c954e9129aa06">Notion</a>
    ·  
  <a href="https://dllflfuvssxc9.cloudfront.net/">Server</a>
    ·
  <a href="https://musin4.snucha.shop">Deployment</a>
  
</div>

<br>



<!-- ABOUT THE PROJECT -->

## :hammer_and_pick: Built with ..
| | :crown: 차동주 | 서보성 | 양찬혁 |
|---|:---:|:---:|:---:|
|Github Id|@dongjoocha|@Boseong-Seo|@yangchanhk98|
|Profile  | | | |
|Role     | - login<br> - auth<br> - 배포<br> (\+Frontend..)| - items<br> - item-inquries<br> - style  | - mypage<br> - review<br> - comment|

<br>

:speaking_head: 6주간 고생 많았던 백엔드 팀원분들 ! 소감 한 마디씩 부탁해요  
> 동주 :    <br>
> 보성 : _"아 리드미 너무 힘들다"_ <br>
> 찬혁 :    <br>



<br>

<!-- Project structure & ERD -->



## :card_file_box: Project Structure
저희 프로젝트는 대략적으로 아래와 같은 구조로 되어 있어요. <br>
각 패키지의 역할은 주석으로 간략하게 작성해뒀어요. 참고 부탁드릴게요 ! <br>
  
노션을 활용해 RESTful하게 API를 구성한 만큼, 이를 잘 담아내기 위해 패키지 구조를 잘 짰다 ..?
```bash
src/main/kotlin/com/wafflestudio/toyproject/team4
├─common       //
├─config       //
├─core         //
│  ├─board
│  ├─image
│  ├─item
│  ├─purchase
│  ├─style
│  └─user
└─oauth       // 
```
그리고 `core`의 (image 패키지를 제외한) 모든 패키지는 아래와 같은 구조를 가지고 있어요.  
  
이때, Controller-Service-Repository 각 layer 단에서 어떤 역할을 하는지 거듭해서 고민하고,  
한 layer에 역할이 가중되지 않도록 주의해가며 코드를 작성했어요.
```bash
├─api
│  ├─request
│  ├─response
│  └─xxController.kt
├─database
│  ├─xxEntity.kt
│  └─xxRepository.kt
├─domain
└─service
   └─xxService.kt
```
  
## :card_file_box: ERD
![ERD](https://user-images.githubusercontent.com/90292371/216746923-864bccf4-a2aa-4ef2-9328-538d2c7c50fd.png)
  
<br>



## :angel: API
저희가 구현한 API를 표로 요약해 나타내자면 다음과 같아요!  
최대한 페이지별로, 기능별로 묶어서 정리했어요     
<br>
이때 자물쇠 표시(:lock:)은, _해당 API에서 `@RequestHeader`로 AccessToken을 요하고 있다_, 라는 의미예요.  
  
보다 자세한 설명은 [**이 노션 페이지**](https://www.notion.so/Rest-API-e92a1784fecf4f42832601e65d1e53b5)를 참고해주세요 :wink:    
  

| Page/Feature        | GET | POST | PUT | PATCH | DELETE | API                              | Authenticated |
|---------------------|:---:|:----:|:---:|:-----:|:------:|--------------------------------------------|:---:|
|**My page**          | ✔️ |      |      | ✔️   |        | /api/user/me                                |🔒  |
|└ My purchases       | ✔️ | ✔️   |     |       |        | /api/user/me/purchases                      |🔒  |
|└ My reviews         | ✔️ |      |      |       |        | /api/user/me/reviews                        |🔒 |
|                     |    | ✔️   | ✔️  |       |        | /api/user/me/review                         |🔒  |
|                     |    |       |     |       | ✔️     | /api/user/me/review/ `review.id`            |🔒 |
|└ My item-inquiries  | ✔️ |      | ✔️  |       |        | /api/user/me/item-inquiries                 |🔒  |
|                     |    |       |     |       | ✔️     | /api/user/me/item-inquiry/`item-inquiry.id` |🔒 |
|└ My recently-viewed | ✔️ | ✔️   |     |       |        | /api/user/me/recently-viewed                |🔒  |
|**My shopping-cart** | ✔️ | ✔️   |     | ✔️    |        | /api/user/me/shopping-cart                 |🔒  |
|                     |    |       |     |       | ✔️     | /api/user/me/shopping-cart/`cart-item.id`  |🔒  |
|**My/Other's closet**| ✔️ |      |      |      |        | /api/user/`user.id`                         |(🔒)|
|                     | ✔️ |      |      |      |        | /api/user/`user.id`/styles                  |    |
|└ Follow             |    | ✔️   |      |       |  ✔️   | /api/user/me/`user.id`/follow               |🔒  |
|                     | ✔️ |      |       |      |       | /api/user/me/`user.id`/followers             |   |
|                     | ✔️ |      |       |      |       | /api/user/me/`user.id`/followings            |   |
|└ Style              |    | ✔️   |      |       |       | /api/style                                   |🔒 |
|**Search**           | ✔️ |      |      |       |       | /api/user/search                             |   |
|                     | ✔️ |      |      |       |       | /api/search                                  |   |
|**Item list page**   | ✔️ |      |      |       |       | /api/items                                   |   |
|**Item detail page** | ✔️ |      |      | ✔️   |        | /api/item/`item.id`                          |   |
|└ Reviews            | ✔️ |      |      | ✔️   |        | /api/item/`item.id`/reviews                  |   |
|  └ Comments         | ✔️ |      |      |      |        | /api/item/`item.id`/comments                |    |
|                     |    | ✔️   |      |      |        | /api/comment                                |    |
|                     |    |      |  ✔️  |      |  ✔️    | /api/comment/`comment.id`                   |    |
|└ Item-inquiries     | ✔️ |      |      |      |        | /api/item/`item.id`/inquiries               |    |
|                     |    | ✔️   |      |      |        | /api/item/`item.id`/inquiry                 |🔒  |
|**Style list page**  | ✔️ |      |      |      |        | /api/styles                                 |    |
|└ Style Modal        | ✔️ |      |      |      |        | /api/style/`style.id`                       |(🔒)|
|  └ Like             |    |  ✔️  |      |      |   ✔️  | /api/style/`style.id`/like                   | 🔒 |
|**Upload Image**     |    |  ✔️  |      |      |        | /api/image-upload                           | 🔒 |

 
  
  
<br>

## :waffle: Getting Started
마지막으로 제일 중요한 :star:로컬 상에서의 실행 방법:star:을 알려드릴게요!  

< ----- 와플 굽는 중 ------ >

### Prerequisites
:white_check_mark: Docker(Desktop)  
:white_check_mark: Docker Compose  
:white_check_mark: DataGrip  
:white_check_mark: intelliJ IDEA  
  
### How to run
1. 현 repository 주소를 본인이 원하는 로컬 상 주소에다가 복제해주세요.
```
git clone https://github.com/wafflestudio20-5/team4-server.git
```

2. 'Docker Desktop'을 실행한 다음, 아래의 명령어를 터미널에 입력해주세요.
``` 
docker-compose up -d
```
  이때, 아래 첨부한 사진과 같이 뜨는지 확인해주세요!

  <img src="https://user-images.githubusercontent.com/74580163/210161547-74fff8db-a2d3-477b-8af9-86a831b71b70.png" alt="docker-compose 실행 화면" width="800" height="450">

3. Run intelliJ, open the repository, and run the application(Team4Application) after build completion

  ![image](https://user-images.githubusercontent.com/74580163/210161740-012a71e3-3d68-49e2-92cd-9b823801cedf.png)

- Run DataGrip and configure to observe the database
  - File -> New -> Data Source -> MySQL
  - Configure as below
  - ❗️ Don't forget to enter password: `team4`

    ![image](https://user-images.githubusercontent.com/74580163/210161569-b67a4db2-e77f-4afa-ae39-208ff4b66f2e.png)
    
  - Check `Test Connection` as well  
    
    ![image](https://user-images.githubusercontent.com/74580163/210161829-e2e0ff9c-b94c-41fe-a2ef-53101757c935.png)
