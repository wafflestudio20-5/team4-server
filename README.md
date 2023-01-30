# team4-server
## Deployment
- AWS EC2: https://musin4.snucha.shop(https://musin4.snucha.shop)


## Prerequisites

- Docker(Desktop)
- Docker Compose
- DataGrip
- intelliJ IDEA

## How to run

- Clone this repository
```
git clone https://github.com/wafflestudio20-5/team4-server.git
```

- Run 'Docker Desktop' and type the following command in terminal
``` 
docker-compose up -d
```

- Check the following

  ![image](https://user-images.githubusercontent.com/74580163/210161547-74fff8db-a2d3-477b-8af9-86a831b71b70.png)

- Run intelliJ, open the repository, and run the application(Team4Application) after build completion

  ![image](https://user-images.githubusercontent.com/74580163/210161740-012a71e3-3d68-49e2-92cd-9b823801cedf.png)

- Run DataGrip and configure to observe the database
  - File -> New -> Data Source -> MySQL
  - Configure as below
  - ❗️ Don't forget to enter password: `team4`

    ![image](https://user-images.githubusercontent.com/74580163/210161569-b67a4db2-e77f-4afa-ae39-208ff4b66f2e.png)
    
  - Check `Test Connection` as well  
    
    ![image](https://user-images.githubusercontent.com/74580163/210161829-e2e0ff9c-b94c-41fe-a2ef-53101757c935.png)
