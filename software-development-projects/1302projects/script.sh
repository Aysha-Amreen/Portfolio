#!/bin/bash -ex
javac -d bin -cp cs1302-urgency-queue.jar src/cs1302/p3/BaseLinkedUrgencyQueue.java
javac -cp bin:cs1302-urgency-queue.jar -d bin src/cs1302/p3/LinkedUrgencyQueue.java
javac -cp bin:cs1302-urgency-queue.jar -d bin src/cs1302/p3/CustomLinkedUrgencyQueue.java
javac -cp bin:cs1302-urgency-queue.jar -d bin src/cs1302/p3/UrgencyQueueTester.java
java -cp bin:cs1302-urgency-queue.jar cs1302.p3.UrgencyQueueTester
check1302 src/cs1302/p3/BaseLinkedUrgencyQueue.java
check1302 src/cs1302/p3/LinkedUrgencyQueue.java
check1302 src/cs1302/p3/CustomLinkedUrgencyQueue.java
