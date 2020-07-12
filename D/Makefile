
CLASSES = \
	And.java \
    BinaryOperation.java \
    Expression.java \
    Impl.java \
    LinkExpression.java \
    Not.java \
    Or.java \
    Parser.java \
    Proof.java \
    ProofLine.java \
    ProofSchemes.java \
    ProofType.java \
    Solver.java \
    TruthTableLine.java \
    Var.java

all:
	javac $(CLASSES)

run:
	java -Xmx490M -Xms490M -cp . Solver

pack:
	zip hw.zip $(CLASSES) Makefile

