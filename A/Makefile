
CLASSES = \
	And.java \
    BinaryOperation.java \
    Expression.java \
    Impl.java \
    Not.java \
    Or.java \
    Parser.java \
    Var.java

all:
	javac $(CLASSES)

run:
	java -cp . Parser

pack:
	zip hw.zip $(CLASSES) Makefile

