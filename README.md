# tag-tracking-server
Tizzy Macgregor

## Build & Run
```
cd tag-tracking-server/
mvn clean install
java -jar target/tag-tracking-server-1.0.jar -h
```
Then you can send some POST requests, for example
```
curl localhost:1917/api/tags -d '{ "user": "user1", "add":["beyhive_member"], "remove": [] }'
curl localhost:1917/api/tags -d '{ "user": "user2", "add":["beyhive_member", "tag1", "tag2"], "remove": ["beyhive_member"] }'
```

NB. 
The port can be set as a command line argument. Otherwise the default port is 1917.
