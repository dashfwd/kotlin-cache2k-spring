package dashfwd.data

data class Person(
        val id:Int,
        val firstName: String,
        val lastName: String,
        val birthYear: Int,
        val category:String
) {
    fun matchesSearchCriteria(searchText: String) =
            searchText.isBlank() ||
                    firstName.toLowerCase().contains(searchText) ||
                    lastName.toLowerCase().contains(searchText) ||
                    birthYear.toString().contains(searchText) ||
                    category.toLowerCase().contains(searchText)
}
