package dashfwd.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dashfwd.data.Person
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.cache.annotation.CacheEvict



@Service
class PersonDataService(
        // Load the JSON from the classpath
        // See https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html
        @Value("classpath:people.json") val peopleResource:Resource
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Cacheable("allPeopleCache", key = "#root.methodName")
    fun loadPeopleDataUsingCache():PeopleData {
        return loadPeopleDataFromDatabase()
    }

    fun loadPeopleDataFromDatabase():PeopleData {
        log.info("Doing slow database lookup")
        Thread.sleep(1500) // intentionally make this slow, pretend this is coming from a slow DB
        return loadPeopleData()
    }

    @CacheEvict("allPeopleCache", allEntries = true)
    fun evictAllCacheValues() {
        // the annotation will cause all entries to be evicted from the cache
    }


    /**
     * Lambda to load and deserialize peopleData.json
     */
    private val loadPeopleData: () -> PeopleData = {
        // "use" automatically closes the stream; like "try-with-resources" in Java
        peopleResource.inputStream.use { input ->

            // "try" in kotlin returns a value
            val peopleResult = try {
                val jsonAsString = IOUtils.toString(input, "UTF-8")

                // Deserialize the JSON to a Kotlin data class
                // This requires the https://github.com/FasterXML/jackson-module-kotlin module
                val result: List<Person> = jacksonObjectMapper().readValue(jsonAsString)
                PeopleData.Success(result)
            } catch (ex: Exception) {
                PeopleData.Error(ex.message ?: "No message", ex)
            }

            // "peopleResult" is the return value of the lambda
            peopleResult
        }
    }

    // "by lazy" is a delegated property; this is initialized at first use
    // See https://kotlinlang.org/docs/reference/delegated-properties.html
//    val peopleData: PeopleData by lazy(loadPeopleData)
}

// We'll use a sealed class here instead of exceptions.  There can be only
// two results: Success or Error.  Clients will be forced to check for success
// before using the data.
// see https://phauer.com/2019/sealed-classes-exceptions-kotlin/
sealed class PeopleData {
    data class Success(val people: List<Person>) : PeopleData()
    data class Error(val message: String, val cause: Exception) : PeopleData()
}