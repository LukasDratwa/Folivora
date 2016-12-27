cd C:/Program Files/MongoDB/Server/3.0/bin 

mongo.exe folivora --shell --eval "db.createUser({user:'folivora', pwd:'3fdonl2igv4onria8', roles:[{role:'dbAdmin', db:'folviora'}, {role:'userAdminAnyDatabase', db:'admin'}, {role:'readWrite', db:'folivora'}]})"
