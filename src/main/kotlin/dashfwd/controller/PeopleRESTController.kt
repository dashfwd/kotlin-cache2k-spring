package dashfwd.springbootdemo.controllers

import dashfwd.data.Person
import dashfwd.service.PersonService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class PeopleRESTController(
    val personService: PersonService
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val sortOrderToField = mapOf(
        0 to "firstName", 1 to "lastName", 2 to "birthYear", 3 to "category"
    )

    /**
     * This example uses the https://datatables.net/ API, which passes parameters
     * as X-WWW-Form-Urlencoded.
     */
    // curl 'http://localhost:8080/api/people' -XPOST -H 'Content-Type: application/x-www-form-urlencoded; charset=UTF-8' --data 'draw=4&search[value]=Bill'
    @PostMapping("/api/people")
    fun apiPeople(
            @RequestParam(name="useCaching",       defaultValue = "false") useCaching:Boolean,
            @RequestParam(name="draw",             defaultValue = "0")   draw:Int,
            @RequestParam(name="order[0][column]", defaultValue = "0")   sortOrder:Int,
            @RequestParam(name="order[0][dir]",    defaultValue = "asc") sortDir:String,
            @RequestParam(name="search[value]",    defaultValue = "")    searchText:String
    ): AjaxPersonListResponse {
        val (people, error) = personService.sortAndFilter(
                searchText = searchText,
                sortBy     = sortOrderToField[sortOrder] ?: "firstName",
                ascending  = (sortDir).equals("asc", true),
                useCaching = useCaching
        )
        return AjaxPersonListResponse(
                draw = draw,
                error = error,
                recordsTotal = people.count(),
                recordsFiltered = people.count(),
                data = people)
    }

    @PostMapping("/api/clearPeopleCache")
    fun clearPeopleCache(
    ): String {
        personService.personDataService.evictAllCacheValues();
        return "OK"
    }


    /**
     * This alternate example passes data as JSON which is deserialized into an instance of AjaxPersonListRequest
     *
     * curl -d '{"searchText":"Bill"}' -H "Content-Type: application/json" -X POST http://localhost:8080/api/people_alt_api
     */
    @PostMapping("/api/people_alt_api")
    fun apiPeople(
            request:HttpServletRequest,
            @RequestBody listRequest: AjaxPersonListRequest
    ): AjaxPersonListResponse {
        val (people, error)  = personService.sortAndFilter(
                searchText = listRequest.searchText,
                sortBy     = listRequest.sortBy ?: "firstName",
                ascending  = listRequest.ascending ?: true,
                useCaching = false
        )
        return AjaxPersonListResponse(
                recordsTotal = people.count(),
                recordsFiltered = people.count(),
                error = error,
                data = people)
    }
}

data class AjaxPersonListRequest (
        val searchText: String,
        val sortBy:String?,
        val ascending: Boolean?
)

data class AjaxPersonListResponse (
        val draw: Int=1,
        val error:String?=null,
        val recordsTotal: Int,
        val recordsFiltered: Int,
        val data: List<Person>
)