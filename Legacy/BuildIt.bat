del *.class
del *.jar
nmake /A
javac ChessApplet.java
jar cf GUIJavaChess.jar *.class 
