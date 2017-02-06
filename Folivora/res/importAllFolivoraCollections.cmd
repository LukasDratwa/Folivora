SET pathToMongoBin=D:/Programme/MongoDB/Server/3.4/bin
SET pathToImportDir=D:/Tomcat-App-Data/Exports
SET mongoDbPassword=3fdonl2igv4onria8
SET mongoDbUser=folivora

cd %pathToMongoBin%
mongoimport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c AdditionalReward -o %pathToImportDir%/AdditionalReward.json
mongoimport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c Feedback -o %pathToImportDir%/Feedback.json
mongoimport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c Message -o %pathToImportDir%/Message.json
mongoimport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c SearchRequest -o %pathToImportDir%/SearchRequest.json
mongoimport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c TokenStorage -o %pathToImportDir%/TokenStorage.json
mongoimport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c Transaction -o %pathToImportDir%/Transaction.json
mongoimport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c User -o %pathToImportDir%/User.json
mongoimport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c UserCredit -o %pathToImportDir%/UserCredit.json

SET pathToMongoBin=null
SET pathToImportDir=null
SET mongoDbPassword=null
SET mongoDbUser=null