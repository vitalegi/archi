$version = Read-Host "Target version"
Write-Output "Version: ${version}"
Read-Host "Press 'Enter' to proceed"

mvn versions:set "-DgenerateBackupPoms=false" "-DnewVersion=${version}"
git add pom.xml
mvn dependency:tree "-DoutputFile=dependencies-tree.txt" "-DoutputEncoding=UTF-8"
git add dependencies-tree.txt
mvn dependency:list "-DoutputFile=dependencies-list.txt" "-DoutputEncoding=UTF-8"

git add dependencies-list.txt
git commit -m "v${version}"
mvn clean package
Copy-Item ".\target\archi-jar-with-dependencies.jar" -Destination "archi.jar"