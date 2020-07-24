# **ping-service-app**

Application for hosts pinging and tracing.

Prerequisites: Oracle JDK 11.0.6 & Maven

Tech stack: Oracle JDK 11.0.6

Application properties are defined in the application.properties, tasks are listed in src/main/resources/tasks.json as well as logging is configured in src/main/resources/log4j2.xml

The scheudling is done with a help of https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/ScheduledExecutorService.html#scheduleAtFixedRate(java.lang.Runnable,long,long,java.util.concurrent.TimeUnit)
The distinctive feature of this application is that it can handle duplicated tasks with the help of https://guava.dev/releases/29.0-jre/api/docs/com/google/common/util/concurrent/Striped.html thus mistakenly added equal tasks are not executed concurrently (implemented for knowledge demonstration purpose only).
