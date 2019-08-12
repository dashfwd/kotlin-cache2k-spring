package dashfwd.service

import dashfwd.data.Person
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PersonService(
        val personDataService: PersonDataService
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    private val sortByOptions = mapOf(
            "firstName" to compareBy(Person::firstName),
            "lastName" to compareBy(Person::lastName),
            "birthYear" to compareBy(Person::birthYear),
            "category" to compareBy(Person::category)
    )

    fun sortAndFilter(sortBy:String, ascending:Boolean, searchText:String, useCaching:Boolean):Pair<List<Person>,String?> {
        val peopleData = when(useCaching) {
            true -> personDataService.loadPeopleDataUsingCache()
            false -> personDataService.loadPeopleDataFromDatabase()
        }
        return when (peopleData) {
            is PeopleData.Error -> {
                log.error(peopleData.message, peopleData.cause)
                Pair(emptyList(), "Error loading data; please try again later.")
            }
            is PeopleData.Success -> {
                val filteredPeople = filterPeople(peopleData.people, searchText)
                Pair(sortPeople(filteredPeople, sortBy, ascending), null)
            }
        }
    }

    private fun filterPeople(people: List<Person>, searchText: String) =
        people.filter { it.matchesSearchCriteria(searchText.trim().toLowerCase()) }

    private fun sortPeople(people: List<Person>, sortBy: String, ascending:Boolean): List<Person> {
        val compareBy = sortByOptions[sortBy] ?: error("Unknown sort mode $sortBy")
        val sortedWith = if (ascending) { compareBy } else { compareBy.reversed() }
        return people.sortedWith(sortedWith)
    }
}

