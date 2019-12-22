# Dynamic storage
All applications want to storage images and files server.
Heroku is a good Cloud for Spring framework but Heroku can't store file in server.
Dynamic storage is a Java lib for using save file.
It can store files in local computer or Cloud storages such as Google cloud and AWS S3.
Destination of source files could selected by using applications.properties.
Dynamic storage support create, remove, and get.

## How to use
Firstly, edit pom.xml to installation lib.
```
        <dependency>
            <groupId>nsc.dynamicstorage</groupId>
            <artifactId>dynamicstorage</artifactId>
            <version>1.0-snapshot</version>
        </dependency>
```
Secondly, edit application.propertes to config file location.

Too Google cloud storage
```
# Google cloud storage
dynamicstorage.provider=google
dynamicstorage.google.bucketname=bucket_name
```

Too local computer
```
# local computer
dynamicstorage.provider=native
```

Lastly, storage file by using Dynamic storage.
```

    @Autowired
    private DynamicStorage dynamicStorage;

    public void saveFile(MultipartFile file){
        String filePath = "pathname/image.png";
        ...
        //save
        dynamicStorage.save(file, filePath);
        ...

        //get
        byte [] data = dynamicStorage.get(fileName);

        //remove
        dynamicStorage.remove(fileName);
    }
```

In Google cloud storage, user can put credentials.json which get from Google cloud storage's (https://cloud.google.com/docs/authentication/getting-started).
