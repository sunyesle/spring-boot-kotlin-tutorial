package com.sunyesle.spring_boot_kotlin_tutorial.user
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/user")
class UserController(private val service: UserService) {

    @GetMapping
    fun findAll() = service.findAll()

    @GetMapping("/{username}")
    fun findOne(@PathVariable username: String) =
        service.findByUsername(username) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "This user does not exist")

    @PostMapping
    fun save(@RequestBody @Valid request: UserSaveRequest): ResponseEntity<UserResponse> {
        val savedUser = service.save(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser)
    }
}
