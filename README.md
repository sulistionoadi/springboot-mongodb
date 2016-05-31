# springboot-mongodb
Contoh membuat project dengan menggunakan spring-boot dan mongodb sebagai data storage

## Build and Run

### Persiapan Database

1. Install Mongodb terlebih dahulu [https://docs.mongodb.com/manual/installation/](https://docs.mongodb.com/manual/installation/)
2. Masuk ke Mongodb Client via command-line / shell / terminal
    ```
    $ mongo [enter]
    MongoDB shell version: 2.6.3
    connecting to: test
    > 
    ```
3. Create atau masuk ke Database **belajar_mongodb**
    ```
    > use belajar_mongodb
    ```
4. Bikin table **users** sekalian insert data user
    ```
    > db.users.save({username:"admin", password:"admin123"})
    ```
5. Manual atau Cara menggunakan MongoDB bisa dilihat di [https://docs.mongodb.com/manual/crud/](https://docs.mongodb.com/manual/crud/)


### Jalankan Aplikasi

1. Clone atau download terlebih dahulu 
    ```
    $ git clone https://github.com/sulistionoadi/springboot-mongodb.git
    ```
2. Compile dan Run
    ```
    $ mvn clean spring-boot:run
    ```
3. Browse ke [http://localhost:8081/mongo/api/user](http://localhost:8081/mongo/api/user)