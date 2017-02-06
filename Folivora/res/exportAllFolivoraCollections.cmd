SET pathToMongoBin=D:/Programme/MongoDB/Server/3.4/bin
SET pathToExportDir=D:/Tomcat-App-Data/Exports
SET mongoDbPassword=3fdonl2igv4onria8
SET mongoDbUser=folivora

cd %pathToMongoBin%
mongoexport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c AdditionalReward -o %pathToExportDir%/AdditionalReward.json
mongoexport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c Feedback -o %pathToExportDir%/Feedback.json
mongoexport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c Message -o %pathToExportDir%/Message.json
mongoexport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c SearchRequest -o %pathToExportDir%/SearchRequest.json
mongoexport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c TokenStorage -o %pathToExportDir%/TokenStorage.json
mongoexport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c Transaction -o %pathToExportDir%/Transaction.json
mongoexport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c User -o %pathToExportDir%/User.json
mongoexport -u folivora -p %mongoDbPassword% --authenticationDatabase folivora -d folivora -c UserCredit -o %pathToExportDir%/UserCredit.json

SET pathToMongoBin=null
SET pathToExportDir=null
SET mongoDbPassword=null
SET mongoDbUser=null

cd D:/Tomcat-App-Data/Batches