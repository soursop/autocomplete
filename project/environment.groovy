// File: environment.groovy
environments {

    inetdev {        
        nexus {
            publicRepository = 'http://localhost:8081/nexus/content/groups/public'
            snapshotRepository = 'http://localhost:8081/nexus/content/repositories/snapshots'
            repository = 'http://localhost:8081/nexus/content/repositories/releases'
            userName = 'admin'
            password = 'admin123'
        }        
    }
    
    dev {
        nexus {
            publicRepository = 'http://localhost:8081/nexus/content/groups/public'
            snapshotRepository = 'http://localhost:8081/nexus/content/repositories/snapshots'
            repository = 'http://localhost:8081/nexus/content/repositories/releases'
            userName = 'admin'
            password = 'admin123'
        }        
    }
    
    rc {
        nexus {
            publicRepository = 'http://localhost:8081/nexus/content/groups/public'
            snapshotRepository = 'http://localhost:8081/nexus/content/repositories/rc-snapshots'
            repository = 'http://localhost:8081/nexus/content/repositories/ncsoft-rc-releases'
        	userName = 'admin'
        	password = 'admin123'
        }
    }
    
    stage {
        nexus {
            publicRepository = 'http://localhost:8081/nexus/content/groups/public'
            snapshotRepository = 'http://localhost:8081/nexus/content/repositories/stage-snapshots'
            repository = 'http://localhost:8081/nexus/content/repositories/stage-releases'
            userName = 'admin'
            password = 'admin123'
        }
    }
    
    live {
        nexus {
            publicRepository = 'http://localhost:8081/nexus/content/groups/public'
            snapshotRepository = 'http://localhost:8081/nexus/content/repositories/snapshots'
            repository = 'http://localhost:8081/nexus/content/repositories/releases'
            userName = 'admin'
            password = 'admin123'
        }   
    }
}    