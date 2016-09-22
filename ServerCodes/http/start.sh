rm -f /hc/usr/apache-tomcat-8.5.4/webapps/ROOT/WEB-INF/classes/*.class
javac -cp .:/hc/usr/apache-tomcat-8.5.4/lib/gson-2.7.jar:/hc/usr/apache-tomcat-8.5.4/lib/servlet-api.jar:/hc/usr/apache-tomcat-8.5.4/lib/mysql-connector-java-5.1.39-bin.jar HelloWorld.java
sh /hc/usr/apache-tomcat-8.5.4/bin/startup.sh 
sh /hc/usr/apache-tomcat-8.5.4/bin/shutdown.sh 

