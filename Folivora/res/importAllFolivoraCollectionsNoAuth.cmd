SET pathToMongoBin=C:/Program Files/MongoDB/Server/3.0/bin
SET pathToImportDir=C:/Users/Lukas/Desktop/Exports
SET mongoDbPassword=3fdonl2igv4onria8
SET mongoDbUser=folivora

cd %pathToMongoBin%
mongo folivora --eval "printjson(db.dropDatabase())"

cd %pathToMongoBin%
mongoimport -d folivora -c AdditionalReward %pathToImportDir%/AdditionalReward.json
mongoimport -d folivora -c Feedback %pathToImportDir%/Feedback.json
mongoimport -d folivora -c Message %pathToImportDir%/Message.json
mongoimport -d folivora -c SearchRequest %pathToImportDir%/SearchRequest.json
mongoimport -d folivora -c TokenStorage %pathToImportDir%/TokenStorage.json
mongoimport -d folivora -c Transaction %pathToImportDir%/Transaction.json
mongoimport -d folivora -c User %pathToImportDir%/User.json
mongoimport -d folivora -c UserCredit %pathToImportDir%/UserCredit.json

SET pathToMongoBin=null
SET pathToImportDir=null
SET mongoDbPassword=null
SET mongoDbUser=null

cd C:/Users/Lukas/Desktop