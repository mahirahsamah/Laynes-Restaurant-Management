all: compile run

CLASSES = \
	Driver.java \
	InterfaceActions.java \
	LaynesGUI.java \
	MenuItem.java

classes = $(CLASSES:.java=.class)

objects = $(CLASSES:.java= )

ifdef OS
	jar-files = ".;Jars\*"
else
	jar-files = ".:Jars/*"
endif

compile: $(CLASSES)
	javac -classpath $(jar-files) $(CLASSES)

run: $(classes)
	java -cp $(jar-files) $(objects)