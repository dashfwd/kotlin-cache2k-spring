# Using Cache2K with Spring Boot


Steps that are required:
1. Add dependencies for Cache2K to your build (in `build.gradle.kts`)
2. Add dependency for `spring-boot-starter-cache` to your build (in `build.gradle.kts`)
3. Add a `CachingConfig.kt` class 
    - Annotate it with `@org.springframework.context.annotation.Configuration`
    - Annotate it with `@org.springframework.cache.annotation.EnableCaching`
    - Create a `fun cacheManager(): CacheManager` method annotated with `@Bean`.  See
       `CachingConfig.kt` in this project for an example.
  
  