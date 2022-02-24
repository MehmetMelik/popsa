# popsa
Popsa photo title generator

Hi,

To be able to run the project, please ensure Java 17 is installed in your system.
I used `gradle` as package manager. If you don't have it, you can just run
```
./gradlew fatJar
```
If gradle is installed:
```
gradle fatJar
```
and this will create a jar file under build/libs folder.
After creating the jar file, to be able to run the project, you can run the following command (while working in project folder):
```
java -jar build/libs/popsa-0.0.1-all.jar 1.csv 
```
`1.csv` file is provided in the project folder.

