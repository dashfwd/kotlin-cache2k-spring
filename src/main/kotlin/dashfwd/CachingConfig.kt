package dashfwd

import org.cache2k.expiry.ExpiryTimeValues
import org.cache2k.extra.spring.SpringCache2kCacheManager
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit
import java.util.function.Function

@Configuration
@EnableCaching
class CachingConfig {

    @Bean
    fun cacheManager(): CacheManager {
        val expiryPolicy = { key:Any, value:Any?, loadTime:Long, oldEntry:Any? ->
            if (value == null) { ExpiryTimeValues.NO_CACHE } else { ExpiryTimeValues.ETERNAL }
        }

        return SpringCache2kCacheManager()
                .defaultSetup { b -> b.entryCapacity(2000) }
                .addCaches(
                        Function { b -> b.name("allPeopleCache").
                                entryCapacity(1).
                                expireAfterWrite(30, TimeUnit.SECONDS).permitNullValues(true).
                                expiryPolicy(expiryPolicy)
                        },
                        Function { b -> b.name("personById").
                                entryCapacity(10).
                                expireAfterWrite(2, TimeUnit.MINUTES).permitNullValues(true).
                                expiryPolicy(expiryPolicy)
                        },
                        // additional example, not used in the demo
                        Function { b -> b.name("example1").
                                eternal(true).
                                entryCapacity(Long.MAX_VALUE)
                        },
                        // additional example, not used in the demo
                        Function { b -> b.name("example2").
                                expireAfterWrite(1, TimeUnit.HOURS)
                                .entryCapacity(10)
                        }
                )
    }
}
