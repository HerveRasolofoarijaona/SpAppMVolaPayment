## MVOLA API avec Spring Boot

### Description

Cette application Spring Boot permet d'interagir avec l'API MVOLA. Elle offre les fonctionnalités suivantes :

* **Génération de tokens d'authentification** : Pour sécuriser les requêtes à l'API MVOLA.
* **Traitement des transactions Mvola** : Spécifiquement les paiements marchands.
* **Gestion des callbacks** : Pour recevoir des notifications sur l'état des transactions.

### Prérequis

* **Java Development Kit (JDK)** 11 ou supérieur : https://www.oracle.com/java/technologies/downloads/
* **Maven** : https://maven.apache.org/ ou **Gradle** : https://gradle.org/

### Installation

1. **Cloner le dépôt** : `git clone https://your-repository-url.git`
2. **Naviguer vers le répertoire du projet** : `cd mvola-api-spring-boot`
3. **Installer les dépendances** :
    * **Maven** : `mvn clean install`
    * **Gradle** : `./gradlew build`

### Configuration

1. **Créer le fichier `application.properties`** à la racine du projet.
2. **Remplacer les valeurs par vos propres identifiants** :

```properties
spring.application.name=apiMvola
mvola.auth.url=[see in documentation]
mvola.transaction.url=[see in documentation]
mvola.client.id=<your client id in prod>
mvola.client.secret=<your_client_secret in prod>
mvola.callbackurl=https://your-domain:your-port
mvola.creditParty=<your merchant number>
mvola.NameEntreprise=<your merchant name>
logging.level.org.springframework=INFO
logging.file.name=application.log
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=admin1234
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=myapp
spring.datasource.url=<you're database url>
spring.datasource.username=<you're database username>
spring.datasource.password=<you're database password>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
security.ignored=/**
