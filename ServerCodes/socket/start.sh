rm -f *.class
javac -cp .:gson-2.7.jar:json.org.jar:mysql-connector-java-5.1.3-bin.jar Server.java
java -cp .:gson-2.7.jar:json.org.jar:mysql-connector-java-5.1.39-bin.jar Server
