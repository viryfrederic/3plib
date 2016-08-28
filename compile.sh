# Remove the last compilation and documentation
echo 'Remove the last release...'
rm -r compiled/classes/*
rm -r release/doc/*
rm -r release/*.jar
echo 'Done !'
# Library compilation
echo 'Compile the library...'
javac -cp release/lib/commons-math3-3.6.1.jar:compiled/classes/ -d compiled/classes src/fr/imag/ppplib/*.java -Xlint:unchecked
echo 'Done !'
# JAR generation
echo 'JAR generation...'
jar cfm release/3plib.jar src/manifest/MANIFEST.MF -C compiled/classes/ .
echo 'Done !'
# Documentation generation
echo 'Documentation generation...'
javadoc -quiet -d release/doc src/fr/imag/ppplib/*.java -classpath release/lib/commons-math3-3.6.1.jar
echo 'Done !'
