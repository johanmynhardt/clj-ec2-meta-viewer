clean:
	rm -rfv build
	rm -rfv pom.xml

pom:
	clj -Spom

build-jar: clean pom
	clj -A:depstar
