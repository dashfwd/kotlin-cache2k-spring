package dashfwd

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinCache2kSpringApplication

fun main(args: Array<String>) {
	val foo ="stuff"
	runApplication<KotlinCache2kSpringApplication>(*args)

	// hey this is a test
}
