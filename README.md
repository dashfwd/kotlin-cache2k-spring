# Cache2K/Spring Boot/Kotlin example
#### Overview ####
This is a Spring Boot project that uses the Cache2K Spring integration 
as described in the [Cache2K documentation](https://cache2k.org/docs/latest/user-guide.html#spring)

#### Running the example server ####
1. Change to the root directory of the project.
2. Run: `./gradlew bootRun`
3. Wait until you see the message `Started SpringApplicationKt` in the logs
4. Open http://localhost:8080 in your browser

The demo shows a list of people with some sorting and filtering options.
In the demo, the data source is very slow; fortunately there's a `Use Caching?` checkbox
that you can click to enable the use of Cache2K.  After you click it, the initial
request will be slow and then all subsequent requests will be fast.  If you unclick
it, the cache is evicted (everything is deleted from the cache) and requests
will go back to being slow.

#### Using Cache2K with Spring Boot ####
The general procedure for setting up Cache2K with Spring can be found [in the Cache2K documentation](https://cache2k.org/docs/latest/user-guide.html#spring).

The procedure below is specific to this project (using Gradle and Kotlin).

Steps that are required:
- Add dependencies for Cache2K to your build (in `build.gradle.kts`); specifically these lines:
```
	val cache2kVersion = "1.2.2.Final"
   	implementation("org.cache2k:cache2k-api:$cache2kVersion")
   	runtimeOnly("org.cache2k:cache2k-core:$cache2kVersion")
   	implementation("org.cache2k:cache2k-spring:$cache2kVersion")  
```
- Add dependency for `spring-boot-starter-cache` to your build (in `build.gradle.kts`)
```
   	implementation("org.springframework.boot:spring-boot-starter-cache")
```
- Add a `CachingConfig.kt` class (The name of the class isn't important)
    - Annotate it with `@org.springframework.context.annotation.Configuration`
    - Annotate it with `@org.springframework.cache.annotation.EnableCaching`
    - Create a `fun cacheManager(): CacheManager` method annotated with `@Bean`.  See
       `CachingConfig.kt` in this project for an example.
  
  
  