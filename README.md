# netty-server

Simple project to store data(PROTOBUF) in kafka.

Requirements :
   1. Java(8)
   2. Zookeeper
   3. Kafka
   4. Protobuf compiler(If u want to change the protobuf object)
   
Step 1 : Java Setup

Step 2 : Installing & Running Zookeeper

          1. Goto your zookeeper config directory. For me its C:\zookeeper-3.4.7\conf
          2. Rename file “zoo_sample.cfg” to “zoo.cfg”
          3. Open zoo.cfg in any text editor like notepad but I’ll prefer notepad++.
          4. Find & edit dataDir=/tmp/zookeeper to dataDir=C:\zookeeper-3.4.7\data
          5. Add entry in System Environment Variables as we did for java
          6. Add in System Variables ZOOKEEPER_HOME = C:\zookeeper-3.4.7
          7. Edit System Variable named “Path” add ;%ZOOKEEPER_HOME%\bin;
          8. You can change the default zookeeper port in zoo.cfg file (Default port 2181).
          9. Run zookeeper by opening a new cmd & type zkserver.
          
Step 3 : Setting Up Kafka

            1. Go to your Kafka config directory. For me its C:\kafka_2.11-0.9.0.0\config
            2. Edit file “server.properties”
            3. Find & edit line “log.dirs=/tmp/kafka-logs” to “log.dir= C:\kafka_2.11-0.9.0.0\kafka-logs”.
            4. If your zookeeper is running on some other machine or cluster you can edit “zookeeper.connect=localhost:2181” to your custom IP & port. For this demo we are using same machine so no need to change. Also Kafka port & broker.id are configurable in this file. Leave other settings as it is.
            5. Your Kafka will run on default port 9092 & connect to zookeeper’s default port which is 2181.
            
Step 4 : Running Kafka Server

              1. Please ensure that your zookeeper is up & running before starting Kafka server.
              2. Go to your kafka installation directory C:\kafka_2.11-0.9.0.0\
              3. Open a command prompt here by pressing Shift + right click and choose “Open command window here” option)
              4. Now type .\bin\windows\kafka-server-start.bat .\config\server.properties & press enter.
              
 About Project :
          It's just simple project to store the protobuf object in kafka. I create a Message protobuf file and compiled it to Java.
          Then I create a file name called "NettyHttpServer.java" to run netty server in port 8080. 
 
 
