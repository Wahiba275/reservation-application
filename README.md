# Table des Matières
- [Introduction](#introduction)
- [Architecture technique du projet](#Architecture-technique-du-projet)
- [Partie Backend](#partie-backend)
  - [Dépendances](#dépendances)
  - [Services](#services)
    - [Config Service](#config-service)
    - [Gateway Service](#gateway-service)
    - [Reservation Service](#reservation-service)
    - [Ressource Service](#ressource-service)
  - [Configurer Keycloak](#configurer-keycloak)

- [Partie Front-end (Angular)](#partie-front-end-angular)
  - [Mise en place d'un Projet Angular avec Authentification Keycloak et Gestion des Réservations et des Ressources](#mise-en-place-dun-projet-angular-avec-authentification-keycloak-et-gestion-des-réservations-et-des-ressources)
  - [Test avant l'utilisation de Keycloak pour l'authentification](#test-avant-lutilisation-de-keycloak-pour-lauthentification)
  - [Test en utilisant Keycloak](#test-en-utilisant-keycloak)
    - [S'authentifier en tant que admin](#sauthentifier-en-tant-que-admin)
    - [S'authentifier en tant que user](#sauthentifier-en-tant-que-user)


# Introduction 
L'objectif de ce projet est de créer une application de gestion de réservations reposant sur une architecture de microservices. Chaque ressource et réservation associée seront gérées de manière indépendante par deux microservices distincts. Cette architecture sera soutenue par des composants techniques tels qu'un service Gateway, un service de découverte, et un service de configuration. La sécurité de l'application sera assurée par OAuth2 et OpenID Connect avec Keycloak comme fournisseur d'identité. Le travail demandé implique la création d'une architecture technique, le développement et les tests des microservices, la sécurisation de l'application avec Keycloak, et enfin, le déploiement avec Docker et Docker Compose. Ce rapport documentera chaque étape du processus pour assurer une compréhension complète du projet.

# Architecture technique du projet

![Alt text](/reservation-captures/archi.PNG)


# Partie Backend

## Dépendances
![Java](https://img.shields.io/badge/Java-red?style=for-the-badge&logo=java)
![Spring](https://img.shields.io/badge/Spring-green?style=for-the-badge&logo=spring)
![Framework](https://img.shields.io/badge/Framework-darkblue?style=for-the-badge)
![Keycloak](https://img.shields.io/badge/Keycloak-yellow?style=for-the-badge&logo=keycloak)
![Angular](https://img.shields.io/badge/Angular-red?style=for-the-badge&logo=angular)
![JWT](https://img.shields.io/badge/JWT-green?style=for-the-badge)
![OAuth2](https://img.shields.io/badge/OAuth2-blue?style=for-the-badge)
![OIDC](https://img.shields.io/badge/OIDC-purple?style=for-the-badge)
![Consul](https://img.shields.io/badge/Consul-blue?style=for-the-badge&logo=consul)

## Services

### Config Service
Le service de configuration est responsable de la gestion des configurations de tous les autres services dans le système. <br>
Pour démarrer le serveur Consul, exécutez la commande suivante : <br>
`consul agent -server -bootstrap-expect=1 -data-dir=consul-data -ui -bind=@IP`<br>
Vous pouvez consulter tous les services enregistrés en accédant à : <br>
`http://localhost:8500/`

Voici les services enregistrés :

![Alt text](/reservation-captures/consul1.PNG)

![Alt text](/reservation-captures/consul2.PNG)

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
1. ***La liste de reservations***
 ```java
 @GetMapping("/Reservations/{id}")
    public Reservation getReservation(@PathVariable String id){
        Reservation reservation  = reservationRepository.findById(id).orElse(null);
        if (reservation != null) {
            Personne personne = personneRestClient.personneById(reservation.getIdPersonne());
            reservation.setPersonne(personne);
        }
        return reservation;
    }
```
![Alt text](/reservation-captures/rs.PNG)

![Alt text](/reservation-captures/rrrrrrrrrrrrrrrrrr.PNG)


3. ***Obtenir une reservations par ID***

```java
@GetMapping("/Reservations")
    public List<Reservation> reservationsList(){
        return reservationService.getReservations();
    }
```

![Alt text](/reservation-captures/rById.PNG)

![Alt text](/reservation-captures/s1.PNG)

![Alt text](/reservation-captures/s2.PNG)

3. ***La liste de personnes***
 ```java
  @GetMapping("/Personnes")
    public List<Personne> getAllPersonnes(){
        List<Personne> personnes = personneRepository.findAll();
        personnes.forEach(personne -> {
            List<Reservation> reservations = reservationRepository.findByIdPersonne(personne.getId());
            personne.setReservations(reservations);
        });
        return personnes;
    }
```
   
![Alt text](/reservation-captures/personnes.PNG)

![Alt text](/reservation-captures/pers.PNG)

4. ***Obtenir une personne par ID***
 ```java
   @GetMapping("/Personnes/{id}")
    public Personne getPersonneById(@PathVariable Long id){
        Personne personne = personneRepository.findById(id).get();
        List<Reservation> reservations = reservationRepository.findByIdPersonne(personne.getId());
        personne.setReservations(reservations);
        return personne;
    }
```

![Alt text](/reservation-captures/pById.PNG)

![Alt text](/reservation-captures/swagger6.PNG)

4. ***Obtenir les reservations d'une personne***
 ```java
  @GetMapping("/Reservations/idPersonne/{id}")
    public List<Reservation> reservationsByIdPersonne(@PathVariable Long id) {
        List<Reservation> reservationList = reservationRepository.findByIdPersonne(id);
        return reservationList;
    }
```

![Alt text](/reservation-captures/reservationByIdPers.PNG)

![Alt text](/reservation-captures/jj.PNG)
### Ressource Service

1. ***La liste de ressources***
 ```java
@GetMapping("/ressources")
    public List<RessourceResponseDTO> getAllRessource() {
        return ressourceService.getRessources();
    }
```
   
![Alt text](/reservation-captures/ressources.PNG)

![Alt text](/reservation-captures/swagger2.PNG)

2. ***Obtenir une ressource par ID***
 ```java
 @GetMapping("/ressources/{id}")
    public RessourceResponseDTO getRessourceById(@PathVariable Long id){
        return ressourceService.getRessourceById(id);
    }
```

![Alt text](/reservation-captures/ressourcesId.PNG)

![Alt text](/reservation-captures/swagger3.PNG)

# Configurer Keycloak 

### Démmarrer keycloak
J'ai démarée keycloak en utilisant docker 

![Alt text](/reservation-captures/keycloak.PNG)

### Connexion en utilisant le nom d'utilisateur et le mot de passe

![Alt text](/reservation-captures/admin.PNG)

### Création d'un realm

![Alt text](/reservation-captures/sdia-realmm.PNG)

### Création des rôles

![Alt text](/reservation-captures/sdia-realmm.PNG)

### Création des utilisateurs, attribution des rôles et définition d'un mot de passe

![Alt text](/reservation-captures/users.PNG)

![Alt text](/reservation-captures/wahibaPass.PNG)

![Alt text](/reservation-captures/roleWahiba.PNG)

### Création d'un client pour l'application angular

![Alt text](/reservation-captures/reservation-client.PNG)

![Alt text](/reservation-captures/reservation-client1.PNG)

### l'enregistrement d'un user

![Alt text](/reservation-captures/userRegistration.PNG)

### mapper prédéfini

![Alt text](/reservation-captures/predeined-mapper.PNG)

### politiques de mot de passe

![Alt text](/reservation-captures/passPolicies.PNG)

### Personnalisation de l'écran de connexion

![Alt text](/reservation-captures/log-screen.PNG)

## Sécuriser Backend
### Sécuriser reservations-service

La première chose à faire est d'ajouter la dépendance suivante :

```xml
<!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-oauth2-resource-server -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
			<version>3.2.1</version>
		</dependency>
```

Ensuite, créez un répertoire pour la sécurité. Ce répertoire contient les deux classes ci-dessous :

```java
package ma.enset.reservationservice.sec;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter=new JwtGrantedAuthoritiesConverter();


    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities,jwt.getClaim("preferred_username"));
    }

    private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String , Object> realmAccess;
        Collection<String> roles;
        if(jwt.getClaim("realm_access")==null){
            return Set.of();
        }
        realmAccess = jwt.getClaim("realm_access");
        roles = (Collection<String>) realmAccess.get("roles");
        return roles.stream().map(role->new SimpleGrantedAuthority(role)).collect(Collectors.toSet());
    }

}
```
Donc **JwtAuthConverter** joue un rôle crucial dans la configuration de la sécurité en personnalisant le processus d'extraction des autorisations à partir des jetons **JWT**, ce qui est essentiel pour l'authentification et l'autorisation dans une application Spring Boot intégrant **OAuth2**.

```java
package ma.enset.reservationservice.sec;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private  JwtAuthConverter jwtAuthConverter;

    public SecurityConfig(JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //.cors(Customizer.withDefaults())
                .authorizeHttpRequests(ar->ar.anyRequest().authenticated())
                .oauth2ResourceServer(o2->o2.jwt(jwt->jwt.jwtAuthenticationConverter(jwtAuthConverter)))
                .headers(h->h.frameOptions(fo->fo.disable()))
                .csrf(csrf->csrf.ignoringRequestMatchers("/h2-console/**"))
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

La classe **SecurityConfig** représente la configuration de sécurité pour l'application Spring Boot intégrant OAuth2. Cette configuration utilise une classe appelée **JwtAuthConverter** pour convertir un jeton **JWT (JSON Web Token)** en une instance d'authentification utilisée par Spring Security. L'application est configurée pour autoriser toutes les requêtes authentifiées, en utilisant le mécanisme **OAuth2** comme serveur de ressources. De plus, des ajustements sont apportés aux en-têtes HTTP pour désactiver la protection contre les attaques de type **Clickjacking** (frameOptions). Le système de gestion des autorisations basé sur les rôles est enrichi en extrayant les rôles spécifiques des ressources du jeton JWT. En ce qui concerne la sécurité **CORS (Cross-Origin Resource Sharing)**, une configuration est définie pour permettre l'accès depuis n'importe quelle origine, méthode, et en-tête. Cette configuration utilise les annotations de Spring, telles que **@Configuration**, **@EnableWebSecurity**, et **@EnableMethodSecurity**, pour définir la configuration de sécurité de l'application.

# Partie Front-end (Angular)

## Mise en place d'un Projet Angular avec Authentification Keycloak et Gestion des Réservations et des Ressources

1. ***Créer un nouveau projet Angular***
   
``
ng new reservation-front-end
``

3. ***installer bootstrap***

``
npm i install bootstrap bootstrap-icons
``

3. ***générer des components***

- Reservation componenet:
  
``
ng g c reservations
``

- ressources componenet:
  
``
ng g c ressources
``

- personnes componenet:
  
``
ng g c personnes
``


4. ***Ajouter cette ligne dans le fichier style.css***
```java
@import "bootstrap-icons/font/bootstrap-icons.min.css";
```

Remplire les sections "styles" et "scripts"

```json
 "styles":[
              "src/styles.css",
              "node_modules/bootstrap/dist/css/bootstrap.min.css"
            ],
            "scripts":[
              "node_modules/bootstrap/dist/js/bootstrap.bundle.js"
            ];
```
Les sections "styles" et "scripts" dans le fichier angular.json sont utilisées pour spécifier les fichiers CSS et JavaScript que vous souhaitez inclure dans votre application Angular.

5. ***Installer le package npm keycloak-angular***
C'est un wrapper Angular pour le client JavaScript Keycloak, qui facilite l'intégration d'Angular avec Keycloak pour l'authentification et l'autorisation.

``
npm i keycloak-angular
``

Puis, on ajoute cette fonction au fichier app.modules.ts

```java
function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:8080',
        realm: 'your-realm',
        clientId: 'your-client-id'
      },
      initOptions: {
        onLoad: 'check-sso',
        silentCheckSsoRedirectUri:
          window.location.origin + '/assets/silent-check-sso.html'
      }
    });
}
```
La fonction initializeKeycloak est  utilisée en conjonction avec APP_INITIALIZER pour initialiser Keycloak au démarrage de l'application. Elle prend en paramètre le service KeycloakService et retourne une fonction qui configure et lance l'initialisation de Keycloak avec des paramètres spécifiques tels que l'URL du serveur, le royaume et l'identifiant du client Angular. Ces paramètres doivent être personnalisés en fonction de la configuration Keycloak. L'utilisation de cette fonction garantit que Keycloak est prêt à être utilisé avant le démarrage effectif de l'application.


Pour garantir que Keycloak peut communiquer à travers l'iframe, on doit servir un fichier HTML statique depuis notre application Angular à l'emplacement spécifié dans silentCheckSsoRedirectUri. D'abord, on créé un fichier appelé silent-check-sso.html dans le répertoire assets de notre application et coller le contenu suivant :

```html
<html>
  <body>
    <script>
      parent.postMessage(location.href, location.origin);
    </script>
  </body>
</html>
```

7. ***générer un service de garde***
``
ng g g guards/auth
``
Un garde est utilisé pour contrôler l'accès à une route ou à un ensemble de routes. Le nom "auth" dans cet exemple suggère qu'il s'agit probablement d'un garde d'authentification, qui est couramment utilisé pour protéger l'accès à certaines parties de l'application aux utilisateurs authentifiés.

8. ***générer un service de sécurité dans l'application Angular***
Ce service utilise Keycloak pour gérer l'authentification et autorisation dans cette application Angular

``
ng g s services/security
``

8. ***Contenu de fichier app.modules.ts***
Ce  module Angular (`AppModule`) est configuré pour utiliser Keycloak afin de gérer l'authentification et l'autorisation dans l'application. Les points clés incluent l'importation des modules nécessaires, la déclaration des composants, la configuration de Keycloak à l'aide du service `KeycloakService`, et l'utilisation de `APP_INITIALIZER` pour initialiser Keycloak au démarrage de l'application.

```java
import {APP_INITIALIZER, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';


import { AppComponent } from './app.component';
import { PersonnesComponent } from './personnes/personnes.component';
import { ReservationsComponent } from './reservations/reservations.component';
import { RessourcesComponent } from './ressources/ressources.component';
import {HttpClientModule} from "@angular/common/http";
import {AppRoutingModule} from "./app-routing.module";
import {RouterModule, Routes} from "@angular/router";
import {KeycloakAngularModule, KeycloakService} from "keycloak-angular";
import {AuthGuard} from "./guards/auth.guard";


function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:8080',
        realm: 'sdia-realm',
        clientId: 'reservation-web-client'
      },
      initOptions: {
        onLoad: 'check-sso',
        silentCheckSsoRedirectUri:
          window.location.origin + '/assets/silent-check-sso.html'
      }
    });
}
@NgModule({
  declarations: [
    AppComponent,
    PersonnesComponent,
    ReservationsComponent,
    RessourcesComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    //RouterModule.forRoot(routes),
    KeycloakAngularModule

  ],
  exports:[RouterModule],
  providers: [{provide : APP_INITIALIZER, deps : [KeycloakService],useFactory : initializeKeycloak, multi : true}],
  bootstrap: [AppComponent]
})
export class AppModule{ }
```

9. ***Contenu du fichier personnes.component.html***
Ce code est conçu pour afficher une liste de personnes dans un tableau, chaque ligne ayant un bouton pour voir les réservations associées à cette personne. 
```html
<div class="p-5">
  <table class="table table-striped">
    <thead>
    <tr>
      <th>ID</th>
      <th>Nom</th>
      <th>Email</th>
      <th>Fonction</th>
    </tr>
    </thead>
    <tbody>
    <tr *ngFor="let p of personnes">
      <td>{{p.id}}</td>
      <td>{{p.nom}}</td>
      <td>{{p.email}}</td>
      <td>{{p.fonction}}</td>
      <td>
        <button class="btn btn-success" (click)="getReservations(p)">Voir Réservations</button>
      </td>
    </tr>
    </tbody>
  </table>
</div>
```
Le composant Angular **PersonnesComponent** est conçu pour récupérer une liste de personnes à partir d'un service REST et afficher ces informations dans une table. De plus, il implémente la fonction **getReservations** pour naviguer vers une autre page **('/reservations')** en passant les informations d'une personne sélectionnée en tant que paramètres de requête.
```java
import { Component, OnInit } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { NavigationExtras, Router } from "@angular/router";

@Component({
  selector: 'app-personnes',
  templateUrl: './personnes.component.html',
  styleUrls: ['./personnes.component.css']
})
export class PersonnesComponent implements OnInit {
  personnes: any;
  infoPersonne: any;

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit() {
    this.http.get("http://localhost:9998/reservations-service/Personnes")
      .subscribe({
        next: value => {
          this.personnes = value;
        },
        error: err => {
          console.log(err);
        }
      });
  }

  getReservations(p: any) {
    const navigationExtras: NavigationExtras = {
      queryParams: {
        data: JSON.stringify(p),
      },
    };

    //la méthode navigate pour naviguer avec les paramètres de requête
    this.router.navigate(['/reservations'], navigationExtras);
  }
}

```
9. ***Afficher une liste de réservations associées à une personne spécifique dans un tableau***

```html
<div class="p-5">
  <table class="table table-striped">
    <thead>
    <tr>
      <th>ID</th><th>Nom</th><th>Contexte</th><th>Date</th><th>Duree</th><th>Personne_id</th><th>ressource_id</th>
    </tr>
    </thead>
    <tbody>
    <tr *ngFor="let r of personneReservations">
      <td>{{r.id}}</td>
      <td>{{r.nom}}</td>
      <td>{{r.contexte}}</td>
      <td>{{r.date | date:'dd/MM/yyyy'}}</td>
      <td>{{r.duree}}</td>
      <td>{{r.idPersonne}}</td>
      <td>{{r.resourceId}}</td>
    </tr>
    </tbody>
  </table>
</div>

```

```java
import { Component, OnInit } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { ActivatedRoute, Router, NavigationExtras } from "@angular/router";

@Component({
  selector: 'app-reservations',
  templateUrl: './reservations.component.html',
  styleUrls: ['./reservations.component.css']
})
export class ReservationsComponent implements OnInit {
  reservations: any;
  idPersonne!: number;
  infoPersonne: any;
  personneReservations: any;

  constructor(private http: HttpClient, private router: Router, private route: ActivatedRoute) {
    // Récupérer les paramètres de requête directement dans le constructeur
    this.route.queryParams.subscribe(params => {
      if (params['data']) {
        this.infoPersonne = JSON.parse(params['data']);
        this.idPersonne = this.infoPersonne.id;
      }
    });
  }

  ngOnInit() {
    // Utiliser idPersonne pour récupérer les réservations
    if (this.idPersonne) {
      this.http.get("http://localhost:9998/reservations-service/Reservations/idPersonne/" + this.idPersonne)
        .subscribe({
          next: data => {
            this.personneReservations = data;
          },
          error: err => {
            console.log('Error:', err);
          }
        });
    }
  }
}
```

11. **Afficher les ressources***
```html
<div class="p-5">
  <table class="table table-striped">
    <thead>
    <tr>
      <th>ID</th>
      <th>Nom</th>
      <th>Type</th>
    </tr>
    </thead>
    <tbody>
    <tr *ngFor="let r of ressources">
      <td>{{r.id}}</td>
      <td>{{r.nom}}</td>
      <td>{{r.typeResource}}</td>
    </tr>
    </tbody>
  </table>
</div>


```

```java
import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-ressources',
  templateUrl: './ressources.component.html',
  styleUrl: './ressources.component.css'
})
export class RessourcesComponent implements OnInit{
  ressources: any;
  constructor(private http:HttpClient) {}
  ngOnInit() {
    this.http.get("http://localhost:9998/resource-service/ressources")
      .subscribe({
        next: value => {
          this.ressources = value;
        },
        error: err => {
          console.log(err);
        }
      });
  }

}
```
## Test avant l'utilisation de Keycloak pour l'authentification
- **Ressources**

![Alt text](/reservation-captures/reser-ang.PNG)

- **Personnes**

![Alt text](/reservation-captures/Pers_ang.PNG)

- **Reservation d'une personne**

![Alt text](/reservation-captures/reser-ang.PNG)

## Test en utilisant Keycloak

### S'authentifier en tant que admin
D'abord, on tape le username et le password

![Alt text](/reservation-captures/logSec.PNG)

Donc, puisque je suis admin je peux voir la liste de personnes et leurs reservations
![Alt text](/reservation-captures/logWahiba.PNG)

![Alt text](/reservation-captures/reser2.PNG)

### S'authentifier en tant que user 

![Alt text](/reservation-captures/user1.PNG)

Donc, dans ce cas user1 peut voir seulement la liste de ressources
![Alt text](/reservation-captures/user1Log.PNG)


# Déploiement de l'application avec Docker et Docker Compose

La première étape consiste à générer les fichiers .jar pour chaque service. Pour ce faire, nous ajoutons un fichier nommé Dockerfile qui contient ce qui suit :

```java
FROM openjdk:17-oracle
VOLUME /tmp
COPY target/*.jar  app.jar
ENTRYPOINT ["java","-jar", "app.jar"]
```

Ensuite, nous exécutons la commande suivante :
``
mvn clean package -DskipTests
``
cette commande nettoie le répertoire de sortie, compile le code source, crée un package distribuable (généralement un fichier JAR), et saute l'exécution des tests unitaires. Cela est couramment utilisé lorsqu'on souhaite rapidement construire l'application sans attendre l'exécution complète des tests.


```
docker build . -t reservation-service:v1
```
![Alt text](/reservation-captures/image-reservation.PNG)

![Alt text](/reservation-captures/image-reservation1.PNG)

cette commande indique à Docker de construire une image à partir des fichiers situés dans le répertoire actuel, en utilisant les instructions du fichier Dockerfile trouvé dans ce répertoire, et d'étiqueter l'image résultante avec le nom "reservation-service" et le tag "v1".

Après cela, nous créons un fichier docker-compose à la racine du projet. Ce fichier doit contenir les services et les conteneurs que nous allons stocker dans Docker Desktop.

```docker
services:
  mysql-db-reservation:
    image: mariadb:10.6
    container_name: mysql-db-reservation
    restart: always
    volumes:
      - mysql_data:/var/lib/mysql
    environment:
      MYSQL_DATABASE: reservation-db
      MYSQL_USER: admin
      MYSQL_PASSWORD: admin
      MYSQL_ROOT_PASSWORD: admin
    ports:
      - "3307:3306"
    healthcheck:
      test: [ "CMD", "mysqladmin" ,"ping", "-h", "localhost" ]
      timeout: 5s
      retries: 10
  phpmyadmin:
    image: phpmyadmin
    restart: always
    depends_on:
      - mysql-db-reservation
    ports:
      - "9898:80"
    environment:
      PMA_HOST: mysql-db-reservation
      PMA_PORT: 3306
      PMA_ARBITRARY: 1
      MYSQL_ROOT_PASSWORD: admin
  consul:
    image: consul:1.15.4
    container_name: consul-container
    ports:
      - "8500:8500"  # Port pour l'interface Web de Consul
      - "8600:8600/udp"  # Port pour le DNS de Consul
    environment:
      - CONSUL_BIND_INTERFACE=eth0  # Interface réseau à utiliser par Consul
      - CONSUL_CLIENT_INTERFACE=eth0  # Interface réseau pour les connexions clientes
    command: consul agent -server -bootstrap-expect=1 -data-dir=consul -ui -bind=192.168.135.110
  gateway-service:
    image: openjdk:17-jdk
    container_name: gateway-container
    ports:
      - "9998:9998"
    volumes:
      - ./gateway-service/target/gateway-service.jar:/app/gateway-service.jar
    command: [ "java", "-jar", "/app/gateway-service.jar" ]
  service-reservation:
    build: C:\Users\LENOVO\Downloads\reservation-service
    container_name: service-reservation
    ports:
      - "8083:8083"
    expose:
      - '8083'
    restart: always
    depends_on:
      mysql-db-reservation:
        condition: service_healthy
    environment:
      - DB_URL=jdbc:mysql://mysql-db-reservation:3306/reservation-db
  postgres-service:
    image: postgres:latest
    container_name: postgres-service
    volumes:
      - postgres_data:/var/lib/postgresql/data
    environment:
      POSTGRES_DB: keycloak
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: admin
    ports:
      - '5432:5432'
    expose:
      - '5432'
    healthcheck:
      test: "exit 0"
  pgadmin4:
    image: dpage/pgadmin4
    container_name: pgadmin4
    restart: always
    ports:
      - "8889:80"
    environment:
      PGADMIN_DEFAULT_USER: admin
      PGADMIN_DEFAULT_EMAIL: wa@gmail.com
      PGADMIN_DEFAULT_PASSWORD: azer
  keycloak:
    image: quay.io/keycloak/keycloak:latest
    environment:
      KC_DB: postgres
      KC_DB_URL: jdbc:postgresql://postgres-service:5432/keycloak
      KC_DB_USERNAME: keycloak
      KC_DB_PASSWORD: k1234
      KEYCLOAK_ADMIN: admin
      KEYCLOAK_ADMIN_PASSWORD: admin
    command:
      - start-dev
    restart: always
    ports:
      - '8080:8080'
    expose:
      - '8080'
    depends_on:
      postgres-service:
        condition: service_healthy
volumes:
  mysql_data:
  postgres_data:
```
La commande ci-dessous démarre les services définis dans le fichier docker-compose.yml en arrière-plan, en reconstruisant les images au besoin. Cette commande est couramment utilisée lors du développement ou du déploiement d'applications composées de plusieurs services Docker.

``
docker compose up -d --build
``
![Alt text](/reservation-captures/containers.PNG)

![Alt text](/reservation-captures/docker.PNG)

- ***Accès a phpMyadmin***
![Alt text](/reservation-captures/php.PNG)

![Alt text](/reservation-captures/phpMyAdmin.PNG)

- ***Accès a pgadmin***

![Alt text](/reservation-captures/pgadmin.PNG)

![Alt text](/reservation-captures/pgadmin.PNG)

![Alt text](/reservation-captures/111.PNG)

![Alt text](/reservation-captures/22.PNG)

![Alt text](/reservation-captures/33.PNG)





