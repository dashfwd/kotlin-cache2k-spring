package dashfwd.kotlincache2kspring

import dashfwd.data.Person
import dashfwd.service.PersonDataService
import org.cache2k.core.HeapCache
import org.springframework.cache.CacheManager
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.Cache
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class KotlinCache2kSpringApplicationTests() {

	@Autowired
	lateinit var personDataService: PersonDataService

	@Autowired
	lateinit var cacheManager: CacheManager

	@Test
	fun testFindById() {

		// Find an item using the service, which handles caching for us.
		val person = personDataService.findById(1) ?:
			error("Could not find person 1 using findById")

		// Find an item using the service, which handles caching for us.
		personDataService.findPersonAgain(person) ?:
			error("Could not find person 1 using findPersonAgain")

		// Use the cacheManager to lookup Spring's "Cache" abstraction,
		// and use it to find the item.
		lookupPersonCache()?.let {cache ->
			cache.get(1) ?: error("There should be an item 1 in the cache")
		}

		// We can also lookup the native cache (Read: Cache2K's class)
		// and use it directly.
		lookupPersonCacheNative()?.let { nativeCache ->
			nativeCache.get(1) ?: error("There should be an item 1 in the cache")
			personDataService.removePersonFromCache(person)
			nativeCache.get(1)?.let {
				error("Person 1 should have been removed from the cache")
			}
		}
	}

	private fun lookupPersonCache(): Cache? =
		cacheManager.getCache("personById")

	private fun lookupPersonCacheNative(): HeapCache<Int, Person>? =
		lookupPersonCache() as? HeapCache<Int,Person>
}
