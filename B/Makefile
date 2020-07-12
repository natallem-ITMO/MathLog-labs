
CLASSES = \
	And.java \
    BinaryOperation.java \
    Expression.java \
    Impl.java \
    Not.java \
    Or.java \
    Parser.java \
    Prover.java \
    Var.java

all:
	javac $(CLASSES)

run:
	java -cp . Prover

pack:
	zip hw.zip $(CLASSES) Makefile

