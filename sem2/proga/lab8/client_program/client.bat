set JAVA_HOME=D:/Programs/Java/jdk-17
set CLASSPATH=.;out/production/client_program;../lib/*
rem start %JAVA_HOME%/bin/java --module-path D:/Projects/javafx-sdk-20.0.1/lib --add-modules javafx.controls,javafx.fxml Main
start %JAVA_HOME%/bin/java --module-path ../lib --add-modules javafx.controls,javafx.fxml Main
pause
