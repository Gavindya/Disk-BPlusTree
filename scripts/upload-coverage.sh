#!/bin/bash

echo "Uploading coverage reports."
cd $TRAVIS_BUILD_DIR
echo $TRAVIS_BUILD_DIR
#echo ls -d */
#files=($(ls ./*/target/site/jacoco/jacoco.xml))

#for file in "${files[@]}"
#do
 #   echo "Uploading ${file}"
    java -cp ~/codacy-coverage-reporter-assembly-latest.jar com.codacy.CodacyCoverageReporter -l Java -r ./target/site/jacoco-aggregate/jacoco.xml
#done
echo "Uploading completed."