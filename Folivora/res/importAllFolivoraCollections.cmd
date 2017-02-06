SET pathToMongoBin=C:/Program Files/MongoDB/Server/3.0/bin
SET pathToImportDir=C:/Users/Lukas/Desktop/Exports
SET mongoDbPassword=3fdonl2igv4onria8
SET mongoDbUser=folivora

cd %pathToMongoBin%
mongo folivora --eval "printjson(db.dropDatabase())"

cd %pathToMongoBin%
mongoimport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c AdditionalReward %pathToImportDir%/AdditionalReward.json
mongoimport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c Feedback %pathToImportDir%/Feedback.json
mongoimport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c Message %pathToImportDir%/Message.json
mongoimport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c SearchRequest %pathToImportDir%/SearchRequest.json
mongoimport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c TokenStorage %pathToImportDir%/TokenStorage.json
mongoimport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c Transaction %pathToImportDir%/Transaction.json
mongoimport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c User %pathToImportDir%/User.json
mongoimport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c UserCredit %pathToImportDir%/UserCredit.json

SET pathToMongoBin=null
SET pathToImportDir=null
SET mongoDbPassword=null
SET mongoDbUser=null

cd C:/Users/Lukas/Desktop