
## Introduction 
L'objectif de ce projet est de créer une application de gestion de réservations reposant sur une architecture de microservices. Chaque ressource et réservation associée seront gérées de manière indépendante par deux microservices distincts. Cette architecture sera soutenue par des composants techniques tels qu'un service Gateway, un service de découverte, et un service de configuration. La sécurité de l'application sera assurée par OAuth2 et OpenID Connect avec Keycloak comme fournisseur d'identité. Le travail demandé implique la création d'une architecture technique, le développement et les tests des microservices, la sécurisation de l'application avec Keycloak, et enfin, le déploiement avec Docker et Docker Compose. Ce rapport documentera chaque étape du processus pour assurer une compréhension complète du projet.

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

![Alt text](/micro-services/customers.PNG)

### Ressource Service

![Alt text](/micro-services/products.PNG)

![Alt text](/micro-services/customers.PNG)


## Configurer Keycloak 

### Démmarrer keycloak
J'ai démarée keycloak en utilisant docker 

![Alt text](/micro-services/products.PNG)

### Connexion en utilisant le nom d'utilisateur et le mot de passe

![Alt text](/micro-services/products.PNG)

### Création d'un realm

![Alt text](/micro-services/products.PNG)

### Création des rôles

### Création des utilisateurs, attribution des rôles et définition d'un mot de passe

### Création d'un client pour l'application angular

### l'enregistrement d'un user

### mapper prédéfini

### politiques de mot de passe

### Personnalisation de l'écran de connexion















