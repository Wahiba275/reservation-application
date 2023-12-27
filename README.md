### Config Service
Le service de configuration est responsable de la gestion des configurations de tous les autres services dans le système. <br>
Pour démarrer le serveur Consul, exécutez la commande suivante : <br>
`consul agent -server -bootstrap-expect=1 -data-dir=consul-data -ui -bind=@IP`<br>
Vous pouvez consulter tous les services enregistrés en accédant à : <br>
`http://localhost:8500/`

Voici les services enregistrés :

![Alt text](/micro-services/services.PNG)

### Gateway Service

```java
package org.sid.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
public class GatewayServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}
	@Bean
	DiscoveryClientRouteDefinitionLocator dynamicRoutes(ReactiveDiscoveryClient rdc , DiscoveryLocatorProperties dlp){
                    return new DiscoveryClientRouteDefinitionLocator(rdc,dlp);
	}
}

````
Le morceau de code que j'ai fourni est une méthode Java utilisant Spring Cloud Gateway avec le client de découverte de Spring Cloud pour permettre un routage dynamique basé sur des services enregistrés auprès d'un registre de services tel que Consul.

### Reservation Service

![Alt text](/micro-services/customers.PNG)

### Ressource Service

![Alt text](/micro-services/products.PNG)













