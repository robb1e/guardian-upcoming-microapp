var resultOFAuth = db.auth("admin","admin");
if(resultOFAuth === 0) {
	var status = rs.status();

	var primary = "";

	for(var i=0; i<status.members.length; i++) { 
		if(status.members[i].stateStr === 'PRIMARY') {
			primary = status.members[i].name;
		} 
	}

	if(primary.length > 0) {
		var conn = new Mongo(primary);
		var adminDb = conn.getDB("admin");
		var initialAuth = adminDb.auth("admin","admin");
		if(initialAuth === 0) { // bit off a hack - establishes whether script has already run
			adminDb.addUser("admin","admin"); 
			adminDb.auth("admin","admin");  
            var flexibleContentDb = conn.getDB("whats-next");
            var flexibleContentTestDB = conn.getDB("whats-next-test");
            flexibleContentDb.addUser("whatsnext","whatsnext");
            flexibleContentTestDB.addUser("whatsnext", "whatsnext");
		}
	}
}
